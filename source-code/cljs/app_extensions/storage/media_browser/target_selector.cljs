
(ns app-extensions.storage.media-browser.target-selector
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
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
  [selector-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button selector-id]
                                 :middle-content [header-label         selector-id]
                                 :end-content    [header-select-button selector-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [header-label-bar :x])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div "target-selector"])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :storage.media-browser/render-target-selector!
  [:ui/add-popup! :storage.media-browser/target-selector
                  {:body   #'body
                   :header #'header}])
