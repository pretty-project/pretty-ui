
(ns pretty-inputs.radio-button.attributes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]
              [re-frame.api            :as r]
              [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]))

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
   :data-line-height    :auto})

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
  [_ {:keys [font-size]} _]
  {:class               :pi-radio-button--option-label
   :data-font-size      font-size
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block})

(defn radio-button-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {{:keys [all]} :border-radius :as button-props} _]
  (-> {:class :pi-radio-button--option-button
       :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius all 0.3)}}
      (pretty-build-kit/border-attributes button-props)))

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
  (let [option-picked? (core.env/option-picked? button-id button-props option)
        on-click-f     (fn [_] (core.side-effects/pick-option! button-id button-props option))]
       (-> {:class         :pi-radio-button--option
            :data-selected option-picked?
            :disabled      disabled?}
           (pretty-build-kit/effect-attributes button-props)
           (pretty-build-kit/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

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
  (let [on-blur-f  (fn [_] (core.side-effects/input-left    button-id button-props))
        on-focus-f (fn [_] (core.side-effects/input-focused button-id button-props))]
       (-> {:class :pi-radio-button--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-build-kit/indent-attributes       button-props)
           (pretty-build-kit/orientation-attributes  button-props)
           (pretty-build-kit/style-attributes        button-props)
           (pretty-build-kit/unselectable-attributes button-props))))

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
      (pretty-build-kit/class-attributes   button-props)
      (pretty-build-kit/outdent-attributes button-props)
      (pretty-build-kit/state-attributes   button-props)))
