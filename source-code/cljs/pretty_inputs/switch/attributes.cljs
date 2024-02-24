
(ns pretty-inputs.switch.attributes
    (:require [dom.api                  :as dom]
              [pretty-attributes.api    :as pretty-attributes]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [re-frame.api             :as r]))

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
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  [_ _ _ _]
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
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size] :as switch-props} _ _]
  (-> {:class               :pi-switch--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}
      (pretty-attributes/text-attributes switch-props)))
      ; + :text-selectable? false

(defn switch-option-thumb-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ switch-props _ _]
  (-> {:class :pi-switch--option-thumb}
      (pretty-attributes/adaptive-border-attributes switch-props 0.75)))

(defn switch-option-track-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [switch-id switch-props option-dex option]
  (let [option-color (pretty-inputs.engine/get-input-option-color switch-id switch-props option-dex option)]
       (-> {:class :pi-switch--option-track}
           (pretty-attributes/background-color-attributes {:fill-color option-color})
           (pretty-attributes/border-attributes switch-props))))

(defn switch-option-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:disabled? (boolean)(opt)}
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-switched (boolean)
  ;  :disabled (boolean)}
  [switch-id {:keys [disabled?] :as switch-props} option-dex option]
  (let [option-selected? (pretty-inputs.engine/input-option-selected? switch-id switch-props option-dex option)
        on-click-f       (fn [_] (pretty-inputs.engine/select-input-option! switch-id switch-props option-dex option))]
       (-> {:class         :pi-switch--option
            :data-switched option-selected?
            :disabled      disabled?}
           (pretty-attributes/effect-attributes switch-props)
           (pretty-attributes/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [switch-id switch-props]
  (let [on-blur-f  (fn [_] (pretty-inputs.engine/input-left    switch-id switch-props))
        on-focus-f (fn [_] (pretty-inputs.engine/input-focused switch-id switch-props))]
       (-> {:class :pi-switch--inner
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-attributes/inner-space-attributes switch-props)
           (pretty-attributes/flex-attributes switch-props)
           (pretty-attributes/style-attributes              switch-props))))

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
      (pretty-attributes/class-attributes  switch-props)
      (pretty-attributes/outer-space-attributes switch-props)
      (pretty-attributes/state-attributes  switch-props)
      (pretty-attributes/theme-attributes   switch-props)))
