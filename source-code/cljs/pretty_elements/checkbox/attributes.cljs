
(ns pretty-elements.checkbox.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

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
  {:class               :pe-checkbox--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto})

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
  (-> {:class               :pe-checkbox--option-label
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
  [_ checkbox-props _]
  (-> {:class            :pe-checkbox--option-button
       :data-icon-family :material-symbols-outlined}
      (pretty-css/border-attributes checkbox-props)))

(defn checkbox-option-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-checked (boolean)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [checkbox-id {:keys [disabled? value-path] :as checkbox-props} option]
  (let [option-checked? @(r/subscribe [:pretty-elements.checkbox/option-checked? checkbox-id checkbox-props option])]
       (merge {:class             :pe-checkbox--option
               :data-checked      option-checked?
               :data-click-effect :targeted}
              (if disabled? {:disabled    true}
                            {:on-click    #(r/dispatch [:pretty-elements.checkbox/toggle-option! checkbox-id checkbox-props option])
                             :on-mouse-up #(dom/blur-active-element!)}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-body-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:options-orientation (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-options-orientation (keyword)
  ;  :data-selectable (keyword)
  ;  :style (map)}
  [_ {:keys [options-orientation style] :as checkbox-props}]
  (-> {:class                    :pe-checkbox--body
       :data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-css/indent-attributes checkbox-props)))

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
  (-> {:class :pe-checkbox}
      (pretty-css/default-attributes checkbox-props)
      (pretty-css/outdent-attributes checkbox-props)))