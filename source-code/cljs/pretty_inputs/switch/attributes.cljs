
(ns pretty-inputs.switch.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:font-size (keyword, px or string)}
  ;
  ; @return (map)
  [_ _]
  {:class               :pi-switch--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto})

(defn switch-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:font-size (keyword, px or string)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size]}]
  {:class               :pi-switch--option-label
   :data-font-size      font-size
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block})

(defn switch-option-track-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:border-radius (map)
  ;   {:all (keyword)(opt)}}
  ;
  ; @return (map)
  ; {}
  [_ {{:keys [all]} :border-radius :as switch-props}]
  (-> {:class :pi-switch--option-track
       :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius all 0.75)}}
      (pretty-build-kit/border-attributes switch-props)))

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
  [switch-id {:keys [border-radius disabled?] :as switch-props} option]
  (let [option-switched? @(r/subscribe [:pretty-inputs.switch/option-switched? switch-id switch-props option])
        on-switch-event  #(r/dispatch  [:pretty-inputs.switch/toggle-option!   switch-id switch-props option])]
       (-> {:class         :pi-switch--option
            :data-switched option-switched?
            :disabled      disabled?}
           (pretty-build-kit/effect-attributes switch-props)
           (pretty-build-kit/mouse-event-attributes {:on-click    on-switch-event
                                                     :on-mouse-up dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-body-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:options-orientation (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-options-orientation (keyword)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [switch-id {:keys [options-orientation style] :as switch-props}]
  (-> {:class                    :pi-switch--body
       :data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-build-kit/indent-attributes switch-props)))

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
