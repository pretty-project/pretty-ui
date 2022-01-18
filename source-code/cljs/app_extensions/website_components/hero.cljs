
(ns app-extensions.website-components.hero
    (:require [mid-fruits.math :as math]
              [x.app-core.api  :as a]))



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
    (if scroll-y {:style {:opacity (math/choose scroll-y 40 0 1)}})
    [:div#x-website-hero--scroll-down-icon (a/dom-value :arrow_downward)]])
