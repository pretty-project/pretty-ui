
(ns pretty-elements.toggle.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.control.api :as pretty-css.control]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.appearance/background-attributes             toggle-props)
      (pretty-css.appearance/border-attributes            toggle-props)
      (pretty-css.content/cursor-attributes            toggle-props)
      (pretty-css.layout/element-size-attributes      toggle-props)
      (pretty-css.control/focus-attributes             toggle-props)
      (pretty-css.layout/indent-attributes            toggle-props)
      (pretty-css.control/anchor-attributes              toggle-props)
      (pretty-css.control/mouse-event-attributes       toggle-props)
      (pretty-css.content/unselectable-text-attributes toggle-props)))

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
      (pretty-css.basic/class-attributes        toggle-props)
      (pretty-css.layout/outdent-attributes      toggle-props)
      (pretty-css.basic/state-attributes        toggle-props)
      (pretty-css.appearance/theme-attributes        toggle-props)
      (pretty-css.layout/wrapper-size-attributes toggle-props)))
