
(ns extensions.website-components.hero
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.math    :as math]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-down
  ; @param (keyword) hero-id
  ; @param (map) hero-props
  ;  {:scroll-y (px)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [height scroll-y top]}]
  [:div#x-website-hero--scroll-down
    (if (some? scroll-y)
        {:style {:opacity (math/choose scroll-y 40 0 1)}})
    [:div#x-website-hero--scroll-down-icon (keyword/to-dom-value :arrow_downward)]])
