
(ns components.section-title.prototypes
    (:require [window-observer.api :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-props-prototype
  ; @param (map) title-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :font-weight (keyword)}
  [title-props]
  (if (window-observer/viewport-width-min? 720)
      (merge title-props {:font-size :5xl :font-weight :bold})
      (merge title-props {:font-size :xl  :font-weight :bold})))
