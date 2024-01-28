
(ns pretty-elements.label.attributes
    (:require [dom.api                            :as dom]
              [metamorphic-content.api            :as metamorphic-content]
              
              [pretty-elements.label.side-effects :as label.side-effects]
              [pretty-elements.label.utils        :as label.utils]
              [pretty-engine.api                  :as pretty-engine]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-info-text-button-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-icon-family (keyword)
  ;  :data-text-selectable (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [label-id _]
  (let [on-click-f #(label.side-effects/toggle-info-text-visibility! label-id)]
       {:class                :pe-label--info-text-button
        :data-click-effect    :opacity
        :data-icon-family     :material-symbols-outlined
        :data-icon-size       :xs
        :data-text-selectable false
        :on-click             on-click-f
        :on-mouse-up          dom/blur-active-element!}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-tooltip-content (string)
  ;  :data-tooltip-position (keyword)
  ;  :on-click (function)
  [label-id label-props]
  {:class                 :pe-label--copyable
   :data-click-effect     :opacity
   :data-tooltip-position :right
   :data-tooltip-content  (metamorphic-content/compose :copy!)
   :on-click              (label.utils/on-copy-f label-id label-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ label-props]
  (-> {:class :pe-label--content}
      (pretty-css.content/unselectable-text-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-body-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [focus-id] :as label-props}]
  (-> {:class               :pe-label--body
       :data-letter-spacing :auto
       :on-mouse-up #(if focus-id (pretty-engine/focus-element! focus-id))}
      (pretty-css.appearance/background-attributes        label-props)
      (pretty-css.appearance/border-attributes       label-props)
      (pretty-css.content/font-attributes         label-props)
      (pretty-css.layout/element-size-attributes label-props)
      (pretty-css.layout/indent-attributes       label-props)
      (pretty-css.accessories/marker-attributes       label-props)
      (pretty-css.layout/flex-attributes          label-props)
      (pretty-css.basic/style-attributes        label-props)
      (pretty-css.accessories/tooltip-attributes      label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {}
  [_ label-props]
  (-> {:class :pe-label}
      (pretty-css.basic/class-attributes        label-props)
      (pretty-css.layout/outdent-attributes      label-props)
      (pretty-css.basic/state-attributes        label-props)
      (pretty-css.appearance/theme-attributes        label-props)
      (pretty-css.layout/wrapper-size-attributes label-props)))
