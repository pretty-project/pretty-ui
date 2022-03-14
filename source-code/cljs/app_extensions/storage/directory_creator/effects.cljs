
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.directory-creator.effects
    (:require [plugins.value-editor.api]
              [app-extensions.storage.directory-creator.events  :as directory-creator.events]
              [app-extensions.storage.directory-creator.queries :as directory-creator.queries]
              [mid-fruits.io                                    :as io]
              [x.app-core.api                                   :as a :refer [r]]
              [x.app-dictionary.api                             :as dictionary]))

              

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/load-creator!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) creator-id
  ; @param (map) creator-props
  ;  {:destination-id (string)}
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! {...}]
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! :my-creator {...}]
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! {:destination-id "..."}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ creator-id creator-props]]
      {:db (r directory-creator.events/store-creator-props! db creator-id creator-props)
       :dispatch [:storage.directory-creator/render-dialog! creator-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/create-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id directory-name]]
      [:sync/send-query! :storage.directory-creator/create-directory!
                         {:query (r directory-creator.queries/get-create-directory-query db creator-id directory-name)
                          :on-success [:item-lister/reload-items! :storage :media]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/render-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id]]
      [:value-editor/load-editor! :storage :directory-name
                                  {:label :directory-name :save-button-label :create!
                                   :initial-value (r dictionary/look-up db :new-directory)
                                   :on-save       [:storage.directory-creator/create-directory! creator-id]
                                   :validator {:f io/directory-name-valid?
                                               :invalid-message :invalid-directory-name
                                               :pre-validate?   true}}]))
