
(ns elements.notification-bubble.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-content-attributes
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-letter-spacing (keyword)
  ;  :style (map)}
  [_ {:keys [style] :as bubble-props}]
  (-> {:style               style
       :data-letter-spacing :auto}
      (pretty-css/font-attributes             bubble-props)
      (pretty-css/element-max-size-attributes bubble-props)
      (pretty-css/element-min-size-attributes bubble-props)
      (pretty-css/indent-attributes           bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-body-attributes
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:selectable? (boolean)}
  ;
  ; @return (map)
  ; {:data-selectable (boolean)}
  [_ {:keys [selectable?] :as bubble-props}]
  (-> {:data-selectable selectable?}
      (pretty-css/border-attributes bubble-props)
      (pretty-css/color-attributes  bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-attributes
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [_ bubble-props]
  (-> {} (pretty-css/default-attributes bubble-props)
         (pretty-css/outdent-attributes bubble-props)))
