(ns eval.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [eval.core-test]))

(doo-tests 'eval.core-test)
