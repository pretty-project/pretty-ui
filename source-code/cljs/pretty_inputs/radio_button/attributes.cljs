
(ns pretty-inputs.radio-button.attributes
    (:require [dom.api                  :as dom]
              [metamorphic-content.api  :as metamorphic-content]
              [pretty-attributes.api    :as pretty-attributes]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [re-frame.api             :as r]))

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
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _ _]
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
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size] :as button-props} _ _]
  (-> {:class               :pi-radio-button--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}
      (pretty-attributes/text-attributes button-props)))
      ; + :text-selectable? false

(defn radio-button-option-thumb-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ button-props _ _]
  (-> {:class :pi-radio-button--option-thumb}
      (pretty-attributes/adaptive-border-attributes button-props 0.3)))

(defn radio-button-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [button-id button-props option-dex option]
  (let [option-color (pretty-inputs.engine/get-input-option-color button-id button-props option-dex option)]
       (-> {:class :pi-radio-button--option-button}
           (pretty-attributes/background-color-attributes {:fill-color option-color})
           (pretty-attributes/border-attributes button-props))))

(defn radio-button-option-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)}
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [button-id {:keys [disabled?] :as button-props} option-dex option]
  (let [option-selected? (pretty-inputs.engine/input-option-selected? button-id button-props option-dex option)
        on-click-f       (fn [_] (pretty-inputs.engine/select-input-option! button-id button-props option-dex option))]
       (-> {:class         :pi-radio-button--option
            :data-selected option-selected?
            :disabled      disabled?}
           (pretty-attributes/effect-attributes button-props)
           (pretty-attributes/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

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
  (let [on-blur-f  (fn [_] (pretty-inputs.engine/input-left    button-id button-props))
        on-focus-f (fn [_] (pretty-inputs.engine/input-focused button-id button-props))]
       (-> {:class :pi-radio-button--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-attributes/indent-attributes button-props)
           (pretty-attributes/flex-attributes   button-props)
           (pretty-attributes/style-attributes         button-props))))

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
      (pretty-attributes/class-attributes  button-props)
      (pretty-attributes/outdent-attributes button-props)
      (pretty-attributes/state-attributes  button-props)
      (pretty-attributes/theme-attributes   button-props)))
