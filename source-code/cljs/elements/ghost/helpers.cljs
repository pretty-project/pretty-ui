
(ns elements.ghost.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {:height (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-block-height (keyword)
  ;  :style (map)}
  [_ {:keys [height style] :as ghost-props}]
  (-> {:data-block-height height
       :style             style}
      (pretty-css/border-attributes ghost-props)
      (pretty-css/indent-attributes ghost-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  [_ ghost-props]
  (-> {} (pretty-css/default-attributes ghost-props)
         (pretty-css/outdent-attributes ghost-props)))
