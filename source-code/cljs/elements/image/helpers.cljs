
(ns elements.image.helpers
    (:require [dom.api               :as dom]
              [elements.image.config :as image.config]
              [elements.image.state  :as image.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-error-f
  ; @ignore
  ;
  ; @param (keyword) image-id
  ;
  ; @return (function)
  [image-id]
  (fn [] (let [image (image-id @image.state/REACT-REFERENCES)]
              (dom/set-element-attribute! image "src" image.config/ERROR-IMAGE))))

(defn set-reference-f
  ; @ignore
  ;
  ; @param (keyword) image-id
  ;
  ; @return (function)
  [image-id]
  (fn [ref] (swap! image.state/REACT-REFERENCES assoc image-id ref)))
