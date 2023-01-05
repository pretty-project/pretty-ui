
(ns templates.item-selector.header.views
    (:require [components.api                            :as components]
              [elements.api                              :as elements]
              [re-frame.api                              :as r]
              [templates.item-selector.header.prototypes :as header.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-items-field
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ; {:disabled? (boolean)(opt)
  ;   Default: false
  ;  :search-field-placeholder (metamorphic-content)(opt)
  ;  :search-keys (keywords in vector)}
  [selector-id {:keys [disabled? search-field-placeholder search-keys]}]
  (let [search-event [:item-lister/search-items! selector-id {:search-keys search-keys}]]
       [:div {:style {:flex-grow 1}}
             [elements/search-field ::search-items-field
                                    {:autoclear?    true
                                     :disabled?     disabled?
                                     :on-empty      search-event
                                     :on-type-ended search-event
                                     :outdent       {:horizontal :xxs :left :xxs}
                                     :placeholder   search-field-placeholder}]]))

(defn- order-by-icon-button
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ; {:disabled? (boolean)(opt)
  ;   Default: false
  ;  :order-by-options (namespaced keywords in vector)}
  [selector-id {:keys [disabled? order-by-options]}]
  [elements/icon-button ::order-by-icon-button
                        {:border-radius :s
                         :disabled?     disabled?
                         :hover-color   :highlight
                         :on-click      [:item-lister/choose-order-by! selector-id {:order-by-options order-by-options}]
                         :preset        :order-by}])

(defn control-bar
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ; {:disabled? (boolean)(opt)
  ;   Default: false
  ;  :order-by-options (namespaced keywords in vector)
  ;  :search-field-placeholder (metamorphic-content)(opt)
  ;  :search-keys (keywords in vector)}
  ;
  ; @usage
  ; [control-bar :my-selector {...}]
  [selector-id bar-props]
  (let [bar-props (header.prototypes/control-bar-props-prototype selector-id bar-props)]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? selector-id])]
               [elements/row ::item-selector-control-bar
                             {:content [:<> [search-items-field   selector-id bar-props]
                                            [order-by-icon-button selector-id bar-props]]}]
               [elements/horizontal-separator {:height :xxl}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-bar
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ; {:label (metamorphic-content)
  ;  :on-close (metamorphic-event)}
  ;
  ; @usage
  ; [label-bar :my-selector {...}]
  [selector-id {:keys [label on-close]}]
  [components/popup-label-bar ::item-selector-label-bar
                              {:primary-button   {:label :save! :on-click [:item-selector/save-selection! selector-id]}
                               :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? selector-id])]
                                                         {:label :abort!  :on-click [:item-selector/abort-autosave! selector-id]}
                                                         {:label :cancel! :on-click on-close})
                               :label label}])
