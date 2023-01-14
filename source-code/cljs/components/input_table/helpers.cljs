
(ns components.input-table.helpers
    (:require [hiccup.api     :as hiccup]
              [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-block-label-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (vector) row-block
  ; [(metamorphic-content) input-label
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-label-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:data-color         :default
   :data-font-size     :x
   :data-font-weight   :medium
   :data-indent-bottom :xs
   :data-indent-left   :xxs
   :data-line-height   :text-block})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style] :as table-props}]
  (-> {:style style}
      (pretty-css/border-attributes table-props)
      (pretty-css/indent-attributes table-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-css/default-attributes table-props)
         (pretty-css/outdent-attributes table-props)))
