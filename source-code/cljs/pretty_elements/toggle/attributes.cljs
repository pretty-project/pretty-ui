
(ns pretty-elements.toggle.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-body-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-text-overflow (keyword)}
  [_ toggle-props]
  (-> {:class              :pe-toggle--body
       :data-click-effect  :opacity
       :data-text-overflow :hidden}
      (pretty-css/border-attributes            toggle-props)
      (pretty-css/color-attributes             toggle-props)
      (pretty-css/cursor-attributes            toggle-props)
      (pretty-css/element-size-attributes      toggle-props)
      (pretty-css/focus-attributes             toggle-props)
      (pretty-css/indent-attributes            toggle-props)
      (pretty-css/href-attributes              toggle-props)
      (pretty-css/mouse-event-attributes       toggle-props)
      (pretty-css/unselectable-text-attributes toggle-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ toggle-props]
  (-> {:class :pe-toggle}
      (pretty-css/class-attributes        toggle-props)
      (pretty-css/outdent-attributes      toggle-props)
      (pretty-css/state-attributes        toggle-props)
      (pretty-css/wrapper-size-attributes toggle-props)))
