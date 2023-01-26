
(ns elements.image.utils
    (:require [dom.api               :as dom]
              [elements.image.config :as image.config]
              [react.api             :as react]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-error-f
  ; @ignore
  ;
  ; @param (keyword) image-id
  ;
  ; @return (function)
  [image-id]
  (fn [] (let [image (react/get-reference image-id)]
              (dom/set-element-attribute! image "src" image.config/ERROR-IMAGE))))
