
(ns extensions.media.item-dialogs
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.io      :as io]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]
              [extensions.media.engine       :as engine]
              [extensions.media.file-storage :as file-storage]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- delete-item-dialog-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) action-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :preset   :cancel-button
                    :on-click [:ui/close-popup! popup-id]}])

(defn- delete-item-dialog-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) action-props
  ;
  ; @return (component)
  [popup-id action-props]
  (let [on-click {:dispatch-n [[:ui/close-popup!                    popup-id]
                               [:file-storage/delete-rendered-item! action-props]]}]
       [elements/button {:keypress {:key-code 13}
                         :preset   :delete-button
                         :on-click on-click}]))

(defn- delete-item-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) action-props
  ;
  ; @return (component)
  [popup-id action-props]
  [:<> [elements/polarity {:start-content [delete-item-dialog-cancel-button popup-id action-props]
                           :end-content   [delete-item-dialog-delete-button popup-id action-props]}]])

(defn- delete-subdirectory-dialog
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) content-props
  ;  {:subdirectory-alias (string)}
  ;
  ; @return (component)
  [_ {:keys [subdirectory-alias]}]
  [:<> [elements/text {:content     :delete-directory?
                       :font-weight :bold}]
       [elements/label {:content     subdirectory-alias
                        :color       :muted
                        :font-weight :bold
                        :font-size   :m
                        :icon        :folder}]])

(defn- delete-file-dialog
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) content-props
  ;  {:file-alias (string)}
  ;
  ; @return (component)
  [_ {:keys [file-alias]}]
  [:<> [elements/text {:content     :delete-file?
                       :font-weight :bold}]
       [elements/label {:content     file-alias
                        :color       :muted
                        :font-weight :bold
                        :font-size   :m
                        :icon        :insert_drive_file}]])

(defn- not-enough-space-to-copy-dialog
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) content-props
  ;  {:space-needed (B)}
  ;
  ; @return (component)
  [_ {:keys [space-needed]}]
  (let [space-needed (io/B->MB space-needed)]
       [:<> [elements/text {:content :there-is-not-enough-space
                            :font-weight :bold
                            :layout :fit}]
            [elements/text {:content     :free-some-space
                            :font-weight :bold
                            :layout :fit
                            :replacements [space-needed "MB"]}]
            [elements/separator {:orientation :horizontal :size :s}]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-storage/render-delete-rendered-subdirectory-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [rendered-directory-id (r file-storage/get-rendered-directory-id db)
            subdirectory-alias    (r engine/get-subdirectory-prop db rendered-directory-id subdirectory-id :directory/alias)]
           [:ui/add-popup! ::delete-subdirectory-dialog
                           {:body {:content #'delete-subdirectory-dialog
                                   :content-props {:subdirectory-alias subdirectory-alias}}
                            :header {:content       #'delete-item-label-bar
                                     :content-props {:selected-item {:directory/id (name subdirectory-id)}}}}])))

(a/reg-event-fx
  :file-storage/render-delete-rendered-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r file-storage/get-rendered-directory-id db)
            file-alias            (r engine/get-file-prop db rendered-directory-id file-id :file/alias)]
           [:ui/add-popup! ::delete-file-dialog
                           {:content       #'delete-file-dialog
                            :content-props {:file-alias file-alias}
                            :label-bar     {:content       #'delete-item-label-bar
                                            :content-props {:selected-item {:file/id (name file-id)}}}}])))

(a/reg-event-fx
  :file-storage/render-not-enough-space-to-copy-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (B) space-needed
  (fn [{:keys [db]} [_ space-needed]]
      [:ui/add-popup! ::not-enough-space-to-copy
                      {:content       #'not-enough-space-to-copy-dialog
                       :content-props {:space-needed space-needed}
                       :label-bar     {:content #'ui/accept-popup-header}}]))

(a/reg-event-fx
  :file-storage/render-copy-rendered-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r file-storage/get-rendered-directory-id db)
            filesize              (r engine/get-file-prop db rendered-directory-id file-id :file/filesize)
            storage-free-capacity (r engine/get-storage-free-capacity db)
            space-needed          (- filesize storage-free-capacity)
            file-link             (db/item-id->document-link file-id :file)
            action-props          {:selected-item file-link}]
           (if (> filesize storage-free-capacity)
               [:file-storage/render-not-enough-space-to-copy-dialog! space-needed]
               [:file-browser/load! {:browser-mode :copy-to-directory
                                     :on-done [:file-storage/copy-rendered-item! action-props]
                                     :value-path (file-storage/settings-item-path :destination-directory-id)}]))))

(a/reg-event-fx
  :file-storage/render-copy-rendered-subdirectory-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [rendered-directory-id (r file-storage/get-rendered-directory-id db)
            content-size          (r engine/get-subdirectory-prop db rendered-directory-id subdirectory-id :directory/content-size)
            storage-free-capacity (r engine/get-storage-free-capacity db)
            space-needed          (- content-size storage-free-capacity)
            subdirectory-link     (db/item-id->document-link subdirectory-id :directory)
            action-props          {:selected-item subdirectory-link}]
           (if (> content-size storage-free-capacity)
               [:file-storage/render-not-enough-space-to-copy-dialog! space-needed]
               [:file-browser/load! {:browser-mode :copy-to-directory
                                     :on-done [:file-storage/copy-rendered-item! action-props]
                                     :value-path (file-storage/settings-item-path :destination-directory-id)}]))))

(a/reg-event-fx
  :file-storage/render-move-rendered-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r file-storage/get-rendered-directory-id db)
            file-link             (db/item-id->document-link file-id :file)
            action-props          {:selected-item file-link}]
           [:file-browser/load! {:browser-mode :move-to-directory
                                 :on-done [:file-storage/move-rendered-item! action-props]
                                 :value-path (file-storage/settings-item-path :destination-directory-id)}])))

(a/reg-event-fx
  :file-storage/render-move-rendered-subdirectory-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [rendered-directory-id (r file-storage/get-rendered-directory-id db)
            subdirectory-link     (db/item-id->document-link subdirectory-id :directory)
            action-props          {:selected-item subdirectory-link}]
           [:file-browser/load! {:browser-mode :move-to-directory
                                 ; Önmagába nem helyezhető át egy mappa
                                 :disabled-directories [subdirectory-id]
                                 :on-done [:file-storage/move-rendered-item! action-props]
                                 :value-path (file-storage/settings-item-path :destination-directory-id)}])))
