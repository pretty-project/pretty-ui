
(ns components.input-block.helpers
    (:require [fruits.hiccup.api :as hiccup]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-label-attributes
  ; @param (keyword) block-id
  ; @param (map) block-props
  ;
  ; @return (map)
  ; {}
  [block-id _]
  {:data-color          :muted
   :data-font-size      :xs
   :data-font-weight    :medium
   :data-indent-left    :xs
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-selectable     false
   :for (hiccup/value block-id "input")})
