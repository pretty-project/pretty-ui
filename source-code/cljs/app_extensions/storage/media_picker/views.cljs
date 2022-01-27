
(ns app-extensions.storage.media-picker.views
    (:require [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [app-plugins.item-browser.api :as item-browser]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id _]
  [elements/button :header-cancel-button
                   {:preset :cancel-button :indent :both :keypress {:key-code 27}
                    :on-click [:ui/close-popup! (keyword/add-namespace :storage picker-id)]}])

(defn header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [label]}]
  [elements/label ::header-label
                  {:content label}])

(defn header-select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button :header-select-button
                   {:preset :select-button :indent :both :keypress {:key-code 13}
                    :on-click []}])

(defn header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button picker-id picker-props]
                                 :middle-content [header-label         picker-id picker-props]
                                 :end-content    [header-select-button picker-id picker-props]}])

(defn header-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [:div#item-lister--header--structure
    [app-plugins.item-browser.views/menu-mode-header :storage :media
                                                     {:new-item-options [:create-directory! :upload-files!]
                                                      :menu-mode? true}]
    [app-plugins.item-lister.views/search-mode-header :storage :media
                                                      {:search-mode? false}]])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id header-props]
  [:<> [header-label-bar picker-id header-props]
       [header-menu      picker-id header-props]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [header-structure picker-id {}])

  ;(let [header-props (a/subscribe [:storage.media-picker/get-header-props picker-id])]
  ;     (fn [] [header-structure picker-id @header-props])])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id body-props]
  [item-browser/body :storage :media {:list-element #'app-extensions.storage.media-browser.views/media-item
                                      :selectable-f (fn [{:keys [mime-type]}] (not= mime-type "storage/directory"))
                                      :select-mode? true}])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-item-selected-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [multiple?]}]
  [elements/label {:color :muted :min-height :s
                   :content (if multiple? :no-items-selected :no-item-selected)}])

(defn media-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [label]}]
  (if label [elements/label {:content label :min-height :s}]))

(defn media-picker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [elements/toggle {:content  [no-item-selected-label picker-id picker-props]
                    :on-click [:item-browser/load-browser! :storage :media]}])

(defn media-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [:<> [media-picker-label  picker-id picker-props]
       [media-picker-button picker-id picker-props]])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:mime-types (strings in vector)(opt)
  ;    Default:
  ;   :multiple? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [storage/media-picker {...}]
  ;
  ; @usage
  ;  [storage/media-picker :my-picker {...}]
  ;
  ; @return (component)
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   [media-picker picker-id picker-props]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/render-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [a/debug!]
  (fn [_ [_ picker-id picker-props]]
      [:ui/add-popup! :storage.media-picker/view
                      {:body   [body   picker-id]
                       :header [header picker-id]
                       :stretch-orientation :vertical}]))
