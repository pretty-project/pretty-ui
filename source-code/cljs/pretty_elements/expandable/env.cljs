
(ns pretty-elements.expandable.env
    (:require [pretty-elements.engine.api :as pretty-elements.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (boolean)
  [expandable-id _]
  (let [surface-id (pretty-elements.engine/element-id->subitem-id expandable-id :surface)]
       (pretty-elements.engine/element-visible? surface-id)))
