
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.views
    (:require [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-saver
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;  {:data-url (string)(opt)
  ;   :filename (string)
  ;   :uri (string)(opt)}
  [_ {:keys [data-url filename uri] :as saver-props}]
  [:a#x-file-saver (if data-url {:download filename :href data-url}
                                {:download filename :href uri})])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [popup-id _]
  [elements/button ::cancel-button
                   {:on-click [:ui/close-popup! popup-id]
                    :keypress {:key-code 27}
                    :preset   :cancel-button}])

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [popup-id saver-props]
  [elements/button ::save-button
                   {:on-click {:fx       [:tools/save-file-accepted popup-id saver-props]
                               :dispatch [:ui/close-popup!          popup-id]}
                    :keypress {:key-code 13}
                    :preset   :save-button}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [popup-id saver-props]
  [elements/horizontal-polarity ::header
                                {:start-content [cancel-button popup-id saver-props]
                                 :end-content   [save-button   popup-id saver-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  ;  {:filename (string)}
  [_ {:keys [filename]}]
  [:<> [elements/text  {:content :save-file? :font-weight :bold}]
       [elements/label {:content filename    :font-weight :bold :color :muted :icon :insert_drive_file
                        :selectable? true}]])
