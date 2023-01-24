
(ns carousel.views
    (:require ["react-responsive-carousel" :refer [Carousel]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @constant (map)
(def DEFAULT-CONFIG {:emulateTouch   true
                     :infiniteLoop   true
                     :showThumbs     false
                     :showIndicators false
                     :showStatus     false})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn plugin
  ; @param (?)(opt) data
  ;
  ; @usage
  ; [plugin ...]
  [& data]
  (let [configurations DEFAULT-CONFIG]
       [:div {:class :p-carousel}
             [:> Carousel configurations data]]))
