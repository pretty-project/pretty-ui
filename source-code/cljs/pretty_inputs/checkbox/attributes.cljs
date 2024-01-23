
(ns pretty-inputs.checkbox.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [pretty-engine.api    :as pretty-engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-checkbox--placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class               :pi-checkbox--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto
   :data-text-color     :muted})

(defn checkbox-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size]} _]
  (-> {:class               :pi-checkbox--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}))

(defn checkbox-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [checkbox-id checkbox-props option]
  (let [option-color (pretty-engine/get-input-option-color checkbox-id checkbox-props option)]
       (-> {:class :pi-checkbox--option-button}
           (pretty-build-kit/border-attributes checkbox-props)
           (pretty-build-kit/color-attributes {:fill-color option-color}))))

(defn checkbox-option-checkmark-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class            :pi-checkbox--option-checkmark
   :data-icon-family :material-symbols-outlined})

(defn checkbox-option-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-checked (boolean)
  ;  :disabled (boolean)}
  [checkbox-id {:keys [disabled?] :as checkbox-props} option]
  (let [option-selected? (pretty-engine/input-option-selected? checkbox-id checkbox-props option)
        on-click-f       (fn [_] (pretty-engine/select-input-option! checkbox-id checkbox-props option))]
       (-> {:class        :pi-checkbox--option
            :data-checked option-selected?
            :disabled     disabled?}
           (pretty-build-kit/effect-attributes checkbox-props)
           (pretty-build-kit/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-body-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-blur (function)
  ;  :on-focus (function)}
  [checkbox-id checkbox-props]
  (let [on-blur-f  (fn [_] (pretty-engine/input-left    checkbox-id checkbox-props))
        on-focus-f (fn [_] (pretty-engine/input-focused checkbox-id checkbox-props))]
       (-> {:class    :pi-checkbox--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-build-kit/indent-attributes       checkbox-props)
           (pretty-build-kit/orientation-attributes  checkbox-props)
           (pretty-build-kit/unselectable-attributes checkbox-props)
           (pretty-build-kit/style-attributes        checkbox-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ checkbox-props]
  (-> {:class :pi-checkbox}
      (pretty-build-kit/class-attributes   checkbox-props)
      (pretty-build-kit/outdent-attributes checkbox-props)
      (pretty-build-kit/state-attributes   checkbox-props)))
