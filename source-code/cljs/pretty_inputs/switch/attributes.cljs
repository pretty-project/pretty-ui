
(ns pretty-inputs.switch.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [re-frame.api         :as r]
              [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]))

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
  [_ {:keys [font-size]} _]
  {:class               :pi-switch--option-label
   :data-font-size      font-size
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block})

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
      (pretty-build-kit/adaptive-border-attributes switch-props 0.75)))

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
  (let [option-color (core.env/get-option-color switch-id switch-props option)]
       (-> {:class :pi-switch--option-track}
           (pretty-build-kit/border-attributes switch-props)
           (pretty-build-kit/color-attributes {:fill-color option-color}))))

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
  (let [option-selected? (core.env/option-selected? switch-id switch-props option)
        on-click-f       (fn [_] (core.side-effects/select-option! switch-id switch-props option))]
       (-> {:class         :pi-switch--option
            :data-switched option-selected?
            :disabled      disabled?}
           (pretty-build-kit/effect-attributes switch-props)
           (pretty-build-kit/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

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
  (let [on-blur-f  (fn [_] (core.side-effects/input-left    switch-id switch-props))
        on-focus-f (fn [_] (core.side-effects/input-focused switch-id switch-props))]
       (-> {:class :pi-switch--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-build-kit/indent-attributes       switch-props)
           (pretty-build-kit/orientation-attributes  switch-props)
           (pretty-build-kit/style-attributes        switch-props)
           (pretty-build-kit/unselectable-attributes switch-props))))

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
      (pretty-build-kit/class-attributes   switch-props)
      (pretty-build-kit/outdent-attributes switch-props)
      (pretty-build-kit/state-attributes   switch-props)))
