
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.file-saver.views
    (:require [elements.api        :as elements]
              [layouts.popup-a.api :as popup-a]))



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
  [:a#file-saver (if data-url {:download filename :href data-url}
                              {:download filename :href uri})])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [_ _]
  [elements/button ::cancel-button
                   {:font-size   :xs
                    :keypress    {:key-code 27}
                    :hover-color :highlight
                    :indent      {:all :xxs}
                    :on-click    [:x.ui/remove-popup! :file-saver/view]
                    :preset      :cancel}])

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  [elements/button ::save-button
                   {:font-size   :xs
                    :keypress    {:key-code 13}
                    :hover-color :highlight
                    :indent      {:all :xxs}
                    :on-click    {:fx       [:file-saver/save-accepted saver-id saver-props]
                                  :dispatch [:x.ui/remove-popup! :file-saver/view]}
                    :preset      :save}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  [elements/horizontal-polarity ::header
                                {:end-content   [save-button   saver-id saver-props]
                                 :start-content [cancel-button saver-id saver-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-file-text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [_ _]
  [elements/text ::save-file-text
                 {:content     :save-file?
                  :font-weight :bold
                  :indent      {:vertical :xs}}])

(defn- filename-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;  {:filename (string)}
  [_ {:keys [filename]}]
  [elements/label ::filename-label
                  {:color       :muted
                   :content     filename
                   :font-weight :bold
                   :icon        :insert_drive_file
                   :indent      {:bottom :s :top :xs :vertical :xs}
                   :line-height :block
                   :selectable? true}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  [elements/column ::body
                   {:content [:<> [save-file-text saver-id saver-props]
                                  [filename-label saver-id saver-props]]
                    :horizontal-align :center}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  [popup-a/layout :file-saver/view
                  {:body   [body   saver-id saver-props]
                   :header [header saver-id saver-props]}])
