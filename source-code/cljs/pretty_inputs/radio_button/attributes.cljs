
(ns pretty-inputs.radio-button.attributes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              
              [pretty-engine.api       :as pretty-engine]
              [re-frame.api            :as r]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.control.api :as pretty-css.control]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.live.api :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-radio-button--placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class               :pi-radio-button--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto
   :data-text-color     :muted})

(defn radio-button-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size] :as button-props} _]
  (-> {:class               :pi-radio-button--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}
      (pretty-css.content/unselectable-text-attributes button-props)))

(defn radio-button-option-thumb-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ button-props _]
  (-> {:class :pi-radio-button--option-thumb}
      (pretty-css.appearance/adaptive-border-attributes button-props 0.3)))

(defn radio-button-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [button-id button-props option]
  (let [option-color (pretty-engine/get-input-option-color button-id button-props option)]
       (-> {:class :pi-radio-button--option-button}
           (pretty-css.appearance/background-attributes {:fill-color option-color})
           (pretty-css.appearance/border-attributes button-props))))

(defn radio-button-option-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [button-id {:keys [disabled?] :as button-props} option]
  (let [option-selected? (pretty-engine/input-option-selected? button-id button-props option)
        on-click-f       (fn [_] (pretty-engine/select-input-option! button-id button-props option))]
       (-> {:class         :pi-radio-button--option
            :data-selected option-selected?
            :disabled      disabled?}
           (pretty-css.live/effect-attributes button-props)
           (pretty-css.control/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-id button-props]
  (let [on-blur-f  (fn [_] (pretty-engine/input-left    button-id button-props))
        on-focus-f (fn [_] (pretty-engine/input-focused button-id button-props))]
       (-> {:class :pi-radio-button--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-css.layout/indent-attributes button-props)
           (pretty-css.layout/flex-attributes   button-props)
           (pretty-css.basic/style-attributes         button-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [_ button-props]
  (-> {:class :pi-radio-button}
      (pretty-css.basic/class-attributes   button-props)
      (pretty-css.layout/outdent-attributes button-props)
      (pretty-css.basic/state-attributes   button-props)
      (pretty-css.appearance/theme-attributes   button-props)))
