
(ns pretty-inputs.switch.attributes
    (:require [dom.api              :as dom]
              [pretty-css.api :as pretty-css]
              [pretty-engine.api    :as pretty-engine]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-switch--placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  ;
  ; @return (map)
  [_ _ _]
  {:class               :pi-switch--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto
   :data-text-color     :muted})

(defn switch-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:font-size (keyword, px or string)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size] :as switch-props} _]
  (-> {:class               :pi-switch--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}
      (pretty-css/unselectable-text-attributes switch-props)))

(defn switch-option-thumb-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ switch-props _]
  (-> {:class :pi-switch--option-thumb}
      (pretty-css/adaptive-border-attributes switch-props 0.75)))

(defn switch-option-track-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [switch-id switch-props option]
  (let [option-color (pretty-engine/get-input-option-color switch-id switch-props option)]
       (-> {:class :pi-switch--option-track}
           (pretty-css/border-attributes switch-props)
           (pretty-css/color-attributes {:fill-color option-color}))))

(defn switch-option-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-switched (boolean)
  ;  :disabled (boolean)}
  [switch-id {:keys [disabled?] :as switch-props} option]
  (let [option-selected? (pretty-engine/input-option-selected? switch-id switch-props option)
        on-click-f       (fn [_] (pretty-engine/select-input-option! switch-id switch-props option))]
       (-> {:class         :pi-switch--option
            :data-switched option-selected?
            :disabled      disabled?}
           (pretty-css/effect-attributes switch-props)
           (pretty-css/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-body-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [switch-id switch-props]
  (let [on-blur-f  (fn [_] (pretty-engine/input-left    switch-id switch-props))
        on-focus-f (fn [_] (pretty-engine/input-focused switch-id switch-props))]
       (-> {:class :pi-switch--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-css/indent-attributes      switch-props)
           (pretty-css/orientation-attributes switch-props)
           (pretty-css/style-attributes       switch-props))))

(defn switch-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ; {}
  [_ switch-props]
  (-> {:class :pi-switch}
      (pretty-css/class-attributes   switch-props)
      (pretty-css/outdent-attributes switch-props)
      (pretty-css/state-attributes   switch-props)))
