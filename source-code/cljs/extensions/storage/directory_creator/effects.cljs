
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.directory-creator.effects
    (:require [plugins.value-editor.api]
              [extensions.storage.directory-creator.events     :as directory-creator.events]
              [extensions.storage.directory-creator.queries    :as directory-creator.queries]
              [extensions.storage.directory-creator.validators :as directory-creator.validators]
              [extensions.storage.directory-creator.views      :as directory-creator.views]
              [plugins.item-browser.api                        :as item-browser]
              [x.app-core.api                                  :as a :refer [r]]
              [x.app-ui.api                                    :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/load-creator!
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
      {:db       (r directory-creator.events/store-creator-props! db creator-id creator-props)
       :dispatch [:storage.directory-creator/render-dialog! creator-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/create-directory!
  (fn [{:keys [db]} [_ creator-id]]
      (let [directory-name (get-in db [:storage :directory-creator/meta-items :directory-name])
            query          (r directory-creator.queries/get-create-directory-query          db creator-id directory-name)
            validator-f   #(r directory-creator.validators/create-directory-response-valid? db creator-id %)]
           {:db       (r ui/fake-process! db 15)
            :dispatch-n [[:ui/close-popup! :storage.directory-creator/view]
                         [:sync/send-query! :storage.directory-creator/create-directory!
                                            {:query query :validator-f validator-f
                                             :on-success [:storage.directory-creator/directory-created         creator-id]
                                             :on-failure [:storage.directory-creator/directory-creation-failed creator-id]}]]})))

(a/reg-event-fx
  :storage.directory-creator/directory-created
  (fn [{:keys [db]} [_ creator-id server-response]]
      ; Ha a sikeres mappalétrehozás után még a célmappa az aktuálisan böngészett elem,
      ; akkor újratölti a listaelemeket.
      (let [browser-id     (get-in db [:storage :directory-creator/meta-items :browser-id])
            destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])]
           (if (r item-browser/browsing-item? db browser-id destination-id)
               [:item-browser/reload-items! browser-id]))))

(a/reg-event-fx
  :storage.directory-creator/directory-creation-failed
  (fn [_ [_ creator-id server-response]]
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/render-bubble! {:body :failed-to-create-directory}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/render-dialog!
  (fn [{:keys [db]} [_ creator-id]]
      [:ui/render-popup! :storage.directory-creator/view
                         {:content [directory-creator.views/view creator-id]}]))
