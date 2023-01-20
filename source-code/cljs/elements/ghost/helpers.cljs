
(ns elements.ghost.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as ghost-props}]
  (-> {:style style}
      (pretty-css/block-size-attributes ghost-props)
      (pretty-css/border-attributes     ghost-props)
      (pretty-css/indent-attributes     ghost-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  [_ ghost-props]
  (-> {} (pretty-css/default-attributes ghost-props)
         (pretty-css/outdent-attributes ghost-props)))
