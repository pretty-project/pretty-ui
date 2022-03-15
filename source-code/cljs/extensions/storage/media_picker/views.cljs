
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.views
    (:require [extensions.storage.media-browser.views :as media-browser.views]
              [plugins.item-browser.api               :as item-browser]
              [plugins.item-browser.core.views        :as plugins.item-browser.core.views]
              [plugins.item-lister.core.views         :as plugins.item-lister.core.views]
              [mid-fruits.css                         :as css]
              [mid-fruits.keyword                     :as keyword]
              [x.app-core.api                         :as a]
              [x.app-elements.api                     :as elements]
              [x.app-media.api                        :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; extensions.storage.media-browser.views
(def directory-item media-browser.views/directory-item)
(def file-item      media-browser.views/file-item)

; plugins.item-browser.core.views
(def menu-mode-header   plugins.item-browser.core.views/menu-mode-header)

; plugins.item-lister.core.views
(def search-mode-header plugins.item-lister.core.views/search-mode-header)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button :header-cancel-button
                   {:preset :cancel-button :indent :both :keypress {:key-code 27}
                    :on-click [:ui/close-popup! :storage.media-picker/view]}])

(defn header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [header-label @(a/subscribe [:item-browser/get-item-label :storage :media])]
       [elements/label ::header-label
                       {:content header-label}]))

(defn header-select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected? @(a/subscribe [:storage.media-picker/no-items-selected?])]
       [elements/button :header-select-button
                        {:disabled? no-items-selected?
                         :preset :select-button :indent :both :keypress {:key-code 13}
                         :on-click [:storage.media-picker/save-selected-items!]}]))

(defn header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button picker-id]
                                 :middle-content [header-label         picker-id]
                                 :end-content    [header-select-button picker-id]}])

(defn header-selection-bar-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected?  @(a/subscribe [:storage.media-picker/no-items-selected?])
        selected-item-count @(a/subscribe [:storage.media-picker/get-selected-item-count])]
       [:<> [elements/label {:content {:content :n-items-selected :replacements [selected-item-count]}
                             :color :muted :min-height :s :font-size :xs}]
            [elements/icon-button {:color :default :preset :close :height :s :disabled? no-items-selected?
                                   :on-click [:storage.media-picker/discard-selection!]}]]))

(defn header-selection-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/row {:content [header-selection-bar-structure picker-id]
                 :horizontal-align :right}])

(defn header-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:div#item-lister--header--structure
    [menu-mode-header   :storage :media]
    [search-mode-header :storage :media]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [:<> [header-label-bar     picker-id]
       [header-menu          picker-id]
       [header-selection-bar picker-id]])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ _ {:keys [id mime-type] :as media-item}]
  (let [file-selected?   @(a/subscribe [:storage.media-picker/file-selected?   media-item])
        file-selectable? @(a/subscribe [:storage.media-picker/file-selectable? media-item])]
       (case mime-type "storage/directory" [elements/toggle {:content  [directory-item media-item {:icon :navigate_next}]
                                                             :on-click [:item-browser/browse-item! :storage :media id]
                                                             :hover-color :highlight}]
                                           (if file-selectable? [elements/toggle {:content  [file-item media-item {:icon (if file-selected? :check_circle_outline :radio_button_unchecked)}]
                                                                                  :on-click [:storage.media-picker/file-clicked media-item]
                                                                                  :hover-color :highlight}]
                                                                [file-item media-item {:disabled? true}]))))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [item-browser/body :storage :media {:list-element #'media-item}])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- n-items-selected-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [multiple?]}]
  (let [no-items-selected?  @(a/subscribe [:storage.media-picker/no-items-selected?])
        selected-item-count @(a/subscribe [:storage.media-picker/get-selected-item-count])]
       [elements/label {:color :muted :min-height :s
                        :content (cond (and multiple? no-items-selected?) :no-items-selected
                                       no-items-selected?                 :no-item-selected
                                       :default {:content :n-items-selected :replacements [selected-item-count]})}]))

(defn media-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [disabled? indent label]}]
  (if label [elements/label {:content label :min-height :s :disabled? disabled? :indent indent}]))

(defn media-picker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id {:keys [disabled? indent] :as picker-props}]
  [elements/toggle {:content  [n-items-selected-label picker-id]
                    :on-click [:storage.media-picker/load-picker! picker-id picker-props]
                    :disabled? disabled?
                    :indent    indent}])

(defn media-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [:<> [media-picker-label  picker-id picker-props]
       [media-picker-button picker-id picker-props]])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [storage/media-picker {...}]
  ;
  ; @usage
  ;  [storage/media-picker :my-picker {...}]
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   [media-picker picker-id picker-props]))
