
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.directory-creator.effects
    (:require [plugins.value-editor.api]
              [extensions.storage.directory-creator.events     :as directory-creator.events]
              [extensions.storage.directory-creator.queries    :as directory-creator.queries]
              [extensions.storage.directory-creator.validators :as directory-creator.validators]
              [mid-fruits.io                                   :as io]
              [plugins.item-browser.api                        :as item-browser]
              [x.app-core.api                                  :as a :refer [r]]
              [x.app-dictionary.api                            :as dictionary]
              [x.app-ui.api                                    :as ui]))



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
      (let [query        (r directory-creator.queries/get-create-directory-query          db creator-id directory-name)
            validator-f #(r directory-creator.validators/create-directory-response-valid? db creator-id %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! :storage.directory-creator/create-directory!
                                         {:query query :validator-f validator-f
                                          :on-success [:storage.directory-creator/directory-created         creator-id]
                                          :on-failure [:storage.directory-creator/directory-creation-failed creator-id]}]})))

(a/reg-event-fx
  :storage.directory-creator/directory-created
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id server-response]]
      ; Ha a sikeres mappalétrehozás után még a célmappa az aktuálisan böngészett elem,
      ; akkor újratölti a listaelemeket.
      (let [browser-id     (get-in db [:storage :directory-creator/meta-items :browser-id])
            destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])]
           (if (r item-browser/browsing-item? db browser-id destination-id)
               [:item-browser/reload-items! browser-id]))))

(a/reg-event-fx
  :storage.directory-creator/directory-creation-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ creator-id server-response]]
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-create-directory}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/render-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id]]
      [:value-editor/load-editor! :storage.directory-name-editor
                                  {:label :directory-name :save-button-label :create!
                                   :initial-value (r dictionary/look-up db :new-directory)
                                   :on-save       [:storage.directory-creator/create-directory! creator-id]
                                   :validator {:f io/directory-name-valid?
                                               :invalid-message :invalid-directory-name
                                               :pre-validate?   true}}]))
