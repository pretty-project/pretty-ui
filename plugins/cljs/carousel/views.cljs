
(ns carousel.views
    (:require ["react-responsive-carousel" :refer [Carousel]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def DEFAULT-CONFIG {:emulateTouch   true
                     :infiniteLoop   true
                     :showThumbs     false
                     :showIndicators false
                     :showStatus     false})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slider
  ; @param (?) data
  [data]
  (let [configurations DEFAULT-CONFIG]
       [:div [:> Carousel configurations
                 data]]))

(defn body
  ; @param (?)(opt) data
  ;
  ; @usage
  ; [body ...]
  [& data]
  [:div [slider data]])
