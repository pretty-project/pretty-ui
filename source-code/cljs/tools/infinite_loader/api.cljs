
(ns tools.infinite-loader.api
    (:require [tools.infinite-loader.side-effects]
              [tools.infinite-loader.helpers :as helpers]
              [tools.infinite-loader.views   :as views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; tools.infinite-loader.helpers
(def intersect? helpers/intersect?)

; tools.infinite-loader.views
(def component views/component)
