(ns konva-cljs.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [konva-cljs.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
