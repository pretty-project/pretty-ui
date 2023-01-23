
(ns elements.ghost.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as ghost-props}]
  (-> {:class :e-ghost--body
       :style style}
      (pretty-css/block-size-attributes ghost-props)
      (pretty-css/border-attributes     ghost-props)
      (pretty-css/indent-attributes     ghost-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {}
  [_ ghost-props]
  (-> {:class :e-ghost}
      (pretty-css/default-attributes ghost-props)
      (pretty-css/outdent-attributes ghost-props)))
