
(ns app-extensions.media.directory-actions
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.io        :as io]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-dictionary.api :as dictionary]
              [x.app-elements.api   :as elements]
              [app-extensions.media.engine :as engine]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A [directory-actions/view ...] komponens és a directory-actions események
;  több névtérben is felhasználásra kerülnek.
;  Pl.: ... .file-browser, ... .file-storage
;  A több névtérben való felhasználás lehetőségének biztosítása miatt
;  a directory-actions eszközök saját különálló névtérbe kerültek.



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :media/create-edited-subdirectory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:namespace (keyword)}
  (fn [{:keys [db]} [_ _ {:keys [namespace]}]]
      (let [rendered-directory-id (r engine/get-rendered-directory-id db namespace)
            action-id             (engine/namespace->query-id namespace)
            action-props          {:destination-directory-id rendered-directory-id}]
           [:tools.editor/edit! :media/alias-editor
                                {:initial-value (r dictionary/look-up db :new-directory)
                                 :label         :directory-name
                                 :on-save       [:media/create-subdirectory! action-id action-props]
                                 :validator     {:f io/directory-name-valid?
                                                 :invalid-message :invalid-directory-name
                                                 :pre-validate? true}
                                 :primary-button-label :create!}])))

(a/reg-event-fx
  :media/upload-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:namespace (keyword)}
  (fn [{:keys [db]} [_ _ {:keys [namespace]}]]
      (let [rendered-directory-id (r engine/get-rendered-directory-id db namespace)
            uploader-props        {:destination-directory-id rendered-directory-id
                                   :namespace namespace}]
           [:file-uploader/load! uploader-props])))

(a/reg-event-fx
  :media/->directory-action-selected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:namespace (keyword)}
  (fn [{:keys [db]} [_ component-id context-props]]
      (let [action-type (r elements/get-input-value db :media/directory-action-select)]
           (case action-type
                 :upload-files-from-device [:media/upload-files!               component-id context-props]
                 :create-directory         [:media/create-edited-subdirectory! component-id context-props]))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#3408
  ;
  ; @param (keyword) component-id
  ; @param (map) button-props
  ;  {:disabled? (boolean)(opt)
  ;   :namespace (keyword)}
  ;
  ; @return (component)
  [component-id button-props]
  [elements/button (merge {:color    :default
                           :icon     :upload_file
                           :on-click [:media/upload-files! component-id button-props]
                           :tooltip  :upload-files!
                           :layout   :icon-button
                           :variant  :transparent}
                          (param button-props))])

(defn create-directory-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#3408
  ;
  ; @param (keyword) component-id
  ; @param (map) button-props
  ;  {:disabled? (boolean)(opt)
  ;   :namespace (keyword)}
  ;
  ; @return (component)
  [component-id button-props]
  [elements/button (merge {:color    :default
                           :icon     :create_new_folder
                           :on-click [:media/create-edited-subdirectory! component-id button-props]
                           :layout   :icon-button
                           :tooltip  :create-directory!
                           :variant  :transparent}
                          (param button-props))])

(defn multi-action-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#3408
  ;  Az [elements/button ...] komponens {:on-click ...} tulajdonsága konstans,
  ;  ezért az elem React-fába történő csatolása után paraméterként nem változtatható.
  ;  Emiatt az {:on-click ...} tulajdonságként átadott esemény számára paraméterként
  ;  nem átadható az aktuálisan kirenderelt mappa azonosítója.
  ;  Ezért szükséges a [directory-actions/multi-action-button ...] komponens felhasználási
  ;  névterét átadni paraméterként, ugyanis a névtér használatával az események a Re-Frame
  ;  adatbázisból kiolvashatják az aktuálisan kirenderelt mappa azonosítóját.
  ;
  ; @param (keyword) component-id
  ; @param (map) button-props
  ;  {:disabled? (boolean)(opt)
  ;   :namespace (keyword)}
  ;
  ; @return (component)
  [component-id button-props]
  (let [on-click-event [:media/render-directory-action-select! component-id button-props]]
       [elements/button :media/directory-action-select
                        (merge {:color      :secondary
                                :icon       :add
                                :layout     :icon-button
                                ; XXX#0134
                                :tooltip    :more-actions
                                :value-path (db/path ::primary :directory-action)
                                :variant    :transparent
                                :on-click   on-click-event}
                               (param button-props))]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :media/render-directory-action-select!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:namespace (keyword)}
  (fn [_ [_ component-id context-props]]
      [:elements/render-select-options! :media/directory-action-select-options
                                        {:autoclear?      true
                                         :on-popup-closed [:media/->directory-action-selected component-id context-props]
                                         :options         [{:label :create-directory!         :value :create-directory}
                                                           {:label :upload-files-from-device! :value :upload-files-from-device}]
                                         :user-cancel?    true
                                         ; XXX#0134
                                         :value-path      (db/path ::primary :directory-action)}]))
