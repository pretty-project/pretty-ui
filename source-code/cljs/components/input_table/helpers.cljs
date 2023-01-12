
(ns components.input-table.helpers
    (:require [hiccup.api :as hiccup]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-label-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (vector) input-props
  ; [(metamorphic-content) label
  ;  (keyword) input-id
  ;  (metamorphic-content) input]
  ;
  ; @return (map)
  ; {}
  [_ _ [_ input-id _]]
  {:data-color       :muted
   :data-font-size   :xs
   :data-font-weight :medium
   :data-indent-left :xs
   :data-line-height :text-block
   :data-selectable  false
   :for (hiccup/value input-id "input")})
