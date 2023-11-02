
(ns elements.switch.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:font-size (keyword)}
  ;
  ; @return (map)
  [_ _]
  {:class               :pe-switch--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto})

(defn switch-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:font-size (keyword)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size]}]
  {:class               :pe-switch--option-label
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
  (-> {:class :pe-switch--option-track
       :style {"--adaptive-border-radius" (pretty-css/adaptive-border-radius all 0.75)}}
      (pretty-css/border-attributes switch-props)))

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
  ;  :data-click-effect (keyword)
  ;  :data-switched (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [switch-id {:keys [border-radius disabled?] :as switch-props} option]
  (let [option-switched? @(r/subscribe [:elements.switch/option-switched? switch-id switch-props option])]
       (merge {:class             :pe-switch--option
               :data-click-effect :targeted
               :data-switched option-switched?}
              (if disabled? {:disabled    true}
                            {:on-click    #(r/dispatch [:elements.switch/toggle-option! switch-id switch-props option])
                             :on-mouse-up #(dom/blur-active-element!)}))))

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
  (-> {:class                    :pe-switch--body
       :data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-css/indent-attributes switch-props)))

(defn switch-attributes
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ; {}
  [_ switch-props]
  (-> {:class :pe-switch}
      (pretty-css/default-attributes switch-props)
      (pretty-css/outdent-attributes switch-props)))
