
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
      (pretty-css/border-attributes ghost-props)
      (pretty-css/indent-attributes ghost-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ ghost-props]
  (-> {:class :e-ghost}
      (pretty-css/default-attributes ghost-props)
      (pretty-css/outdent-attributes ghost-props)
      ; The ghost element uses block height but element width
      (pretty-css/block-size-attributes   (dissoc ghost-props :width))
      (pretty-css/element-size-attributes (dissoc ghost-props :height))))
