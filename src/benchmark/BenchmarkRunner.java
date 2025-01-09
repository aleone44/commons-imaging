package org.apache.commons.imaging.formats.tiff;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName()) // Specifica la classe benchmark
                .forks(1) // Numero di processi separati
                //.warmupIterations(20) // Iterazioni di warm-up
                .measurementIterations(5) // Iterazioni di misura
                .build();

        new Runner(opt).run();
    }
}