
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.views
    (:require [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
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
                   {:keypress {:key-code 27}
                    :indent   {:horizontal :xxs :vertical :xs}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :cancel}])

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [popup-id saver-props]
  [elements/button ::save-button
                   {:keypress {:key-code 13}
                    :indent   {:horizontal :xxs :vertical :xs}
                    :on-click {:fx       [:tools/save-file-accepted popup-id saver-props]
                               :dispatch [:ui/close-popup!          popup-id]}
                    :preset   :save}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [popup-id saver-props]
  [elements/horizontal-polarity ::header
                                {:end-content   [save-button   popup-id saver-props]
                                 :start-content [cancel-button popup-id saver-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-file-text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [_ _]
  [elements/text ::save-file-text
                 {:content     :save-file?
                  :font-weight :bold
                  :indent      {:vertical :xs}}])

(defn- filename-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  ;  {:filename (string)}
  [_ {:keys [filename]}]
  [elements/label ::filename-label
                  {:color       :muted
                   :content     filename
                   :font-weight :bold
                   :icon        :insert_drive_file
                   :indent      {:all :xs}
                   :selectable? true}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  [popup-id saver-props]
  [elements/column ::body
                   {:content [:<> [save-file-text popup-id saver-props]
                                  [filename-label popup-id saver-props]]
                    :horizontal-align :center}])
