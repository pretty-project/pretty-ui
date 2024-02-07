
(ns pretty-elements.expandable.env
    (:require [pretty-elements.engine.api  :as pretty-elements.engine]
              [pretty-elements.surface.env :as surface.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-mounted?
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (boolean)
  [expandable-id _]
  (let [surface-id (pretty-elements.engine/element-id->subitem-id expandable-id :surface)]
       (surface.env/surface-mounted? surface-id)))
