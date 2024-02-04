
(ns pretty-elements.toggle.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api :as pretty-css.live]))

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
  ;  :data-text-overflow (keyword)}
  [_ toggle-props]
  (-> {:class              :pe-toggle--body
       :data-text-overflow :hidden}
      (pretty-css.appearance/background-attributes             toggle-props)
      (pretty-css.appearance/border-attributes            toggle-props)
      (pretty-css.content/cursor-attributes            toggle-props)
      (pretty-css.layout/full-block-size-attributes      toggle-props)
      (pretty-css.control/focus-attributes             toggle-props)
      (pretty-css.control/state-attributes             toggle-props)
      (pretty-css.layout/indent-attributes            toggle-props)
      (pretty-css.live/effect-attributes           toggle-props)
      (pretty-css.control/tab-attributes           toggle-props)
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
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ toggle-props]
  (-> {:class :pe-toggle}
      (pretty-css.basic/class-attributes        toggle-props)
      (pretty-css.layout/outdent-attributes      toggle-props)
      (pretty-css.basic/state-attributes        toggle-props)
      (pretty-css.appearance/theme-attributes        toggle-props)
      (pretty-css.layout/wrapper-size-attributes toggle-props)))
