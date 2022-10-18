/*
 * Program:   tiny_gp.java
 *
 * Author:    Riccardo Poli (email: rpoli@essex.ac.uk)
 *
 */

package agh.genetyczne;

import java.util.*;
import java.io.*;

public class TinyGP {
    DataToExcelWriter excelWriter = new DataToExcelWriter();
    double [] fitness;
    char [][] pop;
    static Random rd = new Random();
    final int
        ADD = 110,
            SUB = 111,
            MUL = 112,
            DIV = 113,
            SIN = 114,
            COS = 115,
            FITNESS_SET_START = ADD,
            FITNESS_SET_END = COS;
    double [] x = new double[FITNESS_SET_START];
    double minRandom, maxRandom;
    char [] program;
    int PC;
    int varNumber, fitnessCases, randomNumber;
    double fitnessBestPop = 0.0, fitnessAvgPop = 0.0;
    long seed;
    double avgLength;
    final int
        MAX_LEN = 10000,
                POP_SIZE = 100000,
                DEPTH   = 5,
                GENERATIONS = 100,
                T_SIZE = 2;
    public final double
        PMUT_PER_NODE  = 0.05,
                       CROSSOVER_PROB = 0.9;
    double [][] targets;

    double run() { /* Interpreter */
        char primitive = program[PC++];
        if ( primitive < FITNESS_SET_START)
            return(x[primitive]);
        switch ( primitive ) {
            case ADD : return( run() + run() );
            case SUB : return( run() - run() );
            case MUL : return( run() * run() );
            case DIV : {
                           double num = run(), den = run();
                           if ( Math.abs( den ) <= 0.001 )
                               return( num );
                           else
                               return( num / den );
                       }
            case SIN: return ( Math.sin( Math.toRadians(run())) );
            case COS: return ( Math.cos( Math.toRadians(run())) );
            default:
                return 0.0;
        }
    }

    int traverse( char [] buffer, int bufferCount ) {
        if ( buffer[bufferCount] < FITNESS_SET_START)
            return( ++bufferCount );

        switch (buffer[bufferCount]) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case SIN:
            case COS:
                return (traverse(buffer, traverse(buffer, ++bufferCount)));
            default:
                return 0;
        }
    }

    void setup_fitness(String fname) {
        try {
            int i,j;
            String line;

            BufferedReader in =
                new BufferedReader(
                        new
                        FileReader(fname));
            line = in.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            varNumber = Integer.parseInt(tokens.nextToken().trim());
            randomNumber = Integer.parseInt(tokens.nextToken().trim());
            minRandom =	Double.parseDouble(tokens.nextToken().trim());
            maxRandom =  Double.parseDouble(tokens.nextToken().trim());
            fitnessCases = Integer.parseInt(tokens.nextToken().trim());
            targets = new double[fitnessCases][varNumber +1];
            if (varNumber + randomNumber >= FITNESS_SET_START)
                System.out.println("too many variables and constants");

            for (i = 0; i < fitnessCases; i ++ ) {
                line = in.readLine();
                tokens = new StringTokenizer(line);
                for (j = 0; j <= varNumber; j++) {
                    targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
                }
            }
            excelWriter.copyTargets(targets);
            excelWriter.writeToExcel();
            in.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("ERROR: Please provide a data file");
            System.exit(0);
        }
        catch(Exception e ) {
            System.out.println("ERROR: Incorrect data format");
            System.exit(0);
        }
    }

    double fitness_function( char [] Prog ) {
        int i, len;
        double result, fit = 0.0;

        len = traverse( Prog, 0 );
        for (i = 0; i < fitnessCases; i ++ ) {
            if (varNumber >= 0) System.arraycopy(targets[i], 0, x, 0, varNumber);
            program = Prog;
            PC = 0;
            result = run();
            fit += Math.abs( result - targets[i][varNumber]);
        }
        return(-fit );
    }

    int grow( char [] buffer, int pos, int max, int depth ) {
        char prim = (char) rd.nextInt(2);
        int one_child;

        if ( pos >= max )
            return( -1 );

        if ( pos == 0 )
            prim = 1;

        if ( prim == 0 || depth == 0 ) {
            prim = (char) rd.nextInt(varNumber + randomNumber);
            buffer[pos] = prim;
            return(pos+1);
        }
        else  {
            prim = (char) (rd.nextInt(FITNESS_SET_END - FITNESS_SET_START + 1) + FITNESS_SET_START);
            switch (prim) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case SIN:
                case COS:
                    buffer[pos] = prim;
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0)
                        return (-1);
                    return (grow(buffer, one_child, max, depth - 1));

                default:
                    return 0;
            }
        }
    }

    int print_indiv( char []buffer, int bufferCounter ) {
        int a1=0, a2;
        if ( buffer[bufferCounter] < FITNESS_SET_START) {
            if ( buffer[bufferCounter] < varNumber)
                System.out.print( "X"+ (buffer[bufferCounter] + 1 )+ " ");
            else
                System.out.print( x[buffer[bufferCounter]]);
            return( ++bufferCounter );
        }
        switch (buffer[bufferCounter]) {
            case ADD:
                System.out.print("(");
                a1 = print_indiv(buffer, ++bufferCounter);
                System.out.print(" + ");
                break;
            case SUB:
                System.out.print("(");
                a1 = print_indiv(buffer, ++bufferCounter);
                System.out.print(" - ");
                break;
            case MUL:
                System.out.print("(");
                a1 = print_indiv(buffer, ++bufferCounter);
                System.out.print(" * ");
                break;
            case DIV:
                System.out.print("(");
                a1 = print_indiv(buffer, ++bufferCounter);
                System.out.print(" / ");
                break;
            case SIN:
                System.out.print("(");
                a1 = print_indiv(buffer, ++bufferCounter);
                System.out.print(" sin ");
                break;
            case COS:
                System.out.print("(");
                a1 = print_indiv(buffer, ++bufferCounter);
                System.out.print(" cos ");
                break;
        }
        a2=print_indiv( buffer, a1 );
        System.out.print( ")");
        return( a2);
    }


    char [] buffer = new char[MAX_LEN];
    char [] create_random_indiv( int depth ) {
        char [] ind;
        int len;

        len = grow( buffer, 0, MAX_LEN, depth );

        while (len < 0 )
            len = grow( buffer, 0, MAX_LEN, depth );

        ind = new char[len];

        System.arraycopy(buffer, 0, ind, 0, len );
        return( ind );
    }

    char [][] create_random_pop(int n, int depth, double [] fitness ) {
        char [][]pop = new char[n][];
        int i;

        for ( i = 0; i < n; i ++ ) {
            pop[i] = create_random_indiv( depth );
            fitness[i] = fitness_function( pop[i] );
        }
        return( pop );
    }


    void stats( double [] fitness, char [][] pop, int gen ) {
        int i, best = rd.nextInt(POP_SIZE);
        int nodeCount = 0;
        fitnessBestPop = fitness[best];
        fitnessAvgPop = 0.0;

        for (i = 0; i < POP_SIZE; i ++ ) {
            nodeCount +=  traverse( pop[i], 0 );
            fitnessAvgPop += fitness[i];
            if ( fitness[i] > fitnessBestPop) {
                best = i;
                fitnessBestPop = fitness[i];
            }
        }
        avgLength = (double) nodeCount / POP_SIZE;
        fitnessAvgPop /= POP_SIZE;
        System.out.print("Generation="+gen+" Avg Fitness="+(-fitnessAvgPop)+
                " Best Fitness="+(-fitnessBestPop)+" Avg Size="+ avgLength +
                "\nBest Individual: ");
        print_indiv( pop[best], 0 );
        System.out.print( "\n");
        System.out.flush();
    }

    int tournament( double [] fitness, int tsize ) {
        int best = rd.nextInt(POP_SIZE), i, competitor;
        double  fitnessBest = -1.0e34;

        for ( i = 0; i < tsize; i ++ ) {
            competitor = rd.nextInt(POP_SIZE);
            if ( fitness[competitor] > fitnessBest ) {
                fitnessBest = fitness[competitor];
                best = competitor;
            }
        }
        return( best );
    }

    int negativeTournament(double [] fitness, int tSize ) {
        int worst = rd.nextInt(POP_SIZE), i, competitor;
        double fworst = 1e34;

        for ( i = 0; i < tSize; i ++ ) {
            competitor = rd.nextInt(POP_SIZE);
            if ( fitness[competitor] < fworst ) {
                fworst = fitness[competitor];
                worst = competitor;
            }
        }
        return( worst );
    }

    char [] crossover( char []parent1, char [] parent2 ) {
        int xo1start, xo1end, xo2start, xo2end;
        char [] offspring;
        int len1 = traverse( parent1, 0 );
        int len2 = traverse( parent2, 0 );
        int lenoff;

        xo1start =  rd.nextInt(len1);
        xo1end = traverse( parent1, xo1start );

        xo2start =  rd.nextInt(len2);
        xo2end = traverse( parent2, xo2start );

        lenoff = xo1start + (xo2end - xo2start) + (len1-xo1end);

        offspring = new char[lenoff];

        System.arraycopy( parent1, 0, offspring, 0, xo1start );
        System.arraycopy( parent2, xo2start, offspring, xo1start,
                (xo2end - xo2start) );
        System.arraycopy( parent1, xo1end, offspring,
                xo1start + (xo2end - xo2start),
                (len1-xo1end) );

        return( offspring );
    }

    char [] mutation( char [] parent, double pmut ) {
        int len = traverse( parent, 0 ), i;
        int mutsite;
        char [] parentCopy = new char [len];

        System.arraycopy( parent, 0, parentCopy, 0, len );
        for (i = 0; i < len; i ++ ) {
            if ( rd.nextDouble() < pmut ) {
                mutsite =  i;
                if ( parentCopy[mutsite] < FITNESS_SET_START)
                    parentCopy[mutsite] = (char) rd.nextInt(varNumber + randomNumber);
                else
                    switch (parentCopy[mutsite]) {
                        case ADD:
                        case SUB:
                        case MUL:
                        case DIV:
                        case SIN:
                        case COS:
                            parentCopy[mutsite] =
                                    (char) (rd.nextInt(FITNESS_SET_END - FITNESS_SET_START + 1)
                                            + FITNESS_SET_START);
                    }
            }
        }
        return( parentCopy );
    }

    void printParameters() {
        System.out.print("-- TINY GP (Java version) --\n");
        System.out.print("SEED="+seed+"\nMAX_LEN="+MAX_LEN+
                "\nPOPSIZE="+ POP_SIZE +"\nDEPTH="+DEPTH+
                "\nCROSSOVER_PROB="+CROSSOVER_PROB+
                "\nPMUT_PER_NODE="+PMUT_PER_NODE+
                "\nMIN_RANDOM="+ minRandom +
                "\nMAX_RANDOM="+ maxRandom +
                "\nGENERATIONS="+GENERATIONS+
                "\nTSIZE="+ T_SIZE +
                "\n----------------------------------\n");
    }

    public TinyGP(String fname, long s ) {
        fitness =  new double[POP_SIZE];
        seed = s;
        if ( seed >= 0 )
            rd.setSeed(seed);
        setup_fitness(fname);
        for (int i = 0; i < FITNESS_SET_START; i ++ )
            x[i]= (maxRandom - minRandom)*rd.nextDouble()+ minRandom;
        pop = create_random_pop(POP_SIZE, DEPTH, fitness );
    }

    void evolve() {
        int gen, indivs, offspring, parent1, parent2, parent;
        double newFit;
        char []newInd;
        printParameters();
        stats( fitness, pop, 0 );
        for ( gen = 1; gen < GENERATIONS; gen ++ ) {
            if (  fitnessBestPop > -1e-5 ) {
                System.out.print("PROBLEM SOLVED\n");
                System.exit( 0 );
            }
            for (indivs = 0; indivs < POP_SIZE; indivs ++ ) {
                if ( rd.nextDouble() < CROSSOVER_PROB  ) {
                    parent1 = tournament( fitness, T_SIZE);
                    parent2 = tournament( fitness, T_SIZE);
                    newInd = crossover( pop[parent1],pop[parent2] );
                }
                else {
                    parent = tournament( fitness, T_SIZE);
                    newInd = mutation( pop[parent], PMUT_PER_NODE );
                }
                newFit = fitness_function( newInd );
                offspring = negativeTournament( fitness, T_SIZE);
                pop[offspring] = newInd;
                fitness[offspring] = newFit;
            }
            stats( fitness, pop, gen );
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
        System.exit( 1 );
    }
}