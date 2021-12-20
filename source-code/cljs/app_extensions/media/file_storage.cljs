
(ns app-extensions.media.file-storage
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.io         :as io]
              [mid-fruits.map        :as map]
              [mid-fruits.time       :as time]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-dictionary.api  :as dictionary]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-layouts.api     :as layouts]
              [x.app-media.api       :as media]
              [x.app-router.api      :as router]
              [x.app-sync.api        :as sync]
              [x.app-ui.api          :as ui]
              [app-plugins.item-lister.api        :as item-lister]
              [app-plugins.item-browser.api       :as item-browser]
              [app-extensions.media.context-menu      :as context-menu]
              [app-extensions.media.directory-actions :as directory-actions]
              [app-extensions.media.engine            :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name current-route-directory-id
;  A current-directory-id azonosító a :directory-id route-path-param pillanatnyi
;  értéke, ami a route megváltozásakor még a route esemény megtörténése előtt megváltozik.
;
; @name rendered-directory-id
;
; @name loading-directory-id
;
; @name rendered-file
;
; @name rendered-subdirectory



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A fájl-intéző az [:router/go-to! "/file-storage"] vagy az
;  [:router/go-to! "/file-storage/directory-id"] események meghívásával indítható.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
;  :by-date, :by-name, :by-size
(def DEFAULT-ORDER-BY :by-name)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn settings-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of keywords) xyz
  ;
  ; @return (meta-item-path vector)
  [& xyz]
  (vector/concat-items [::primary :meta-items] xyz))

(defn- file-storage-subdirectory-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;
  ; @return (maps in vector)
  [_ _ subdirectory-id _]
  (let [element-id (engine/item-id->element-id subdirectory-id :file-storage)]
       [{:icon     :drive_file_rename_outline
         :on-click [:file-storage/edit-rendered-subdirectory-alias! subdirectory-id]
         :tooltip  :rename-directory!}
        {:icon     :delete_outline
         :on-click [:file-storage/render-delete-rendered-subdirectory-dialog! subdirectory-id]
         :tooltip  :delete-directory!}
        {:icon     :more_vert
         :on-click [:elements/render-context-surface! element-id]
         :tooltip  :more-options}]))

(defn- file-storage-file-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;
  ; @return (maps in vector)
  [_ _ file-id _]
  (let [element-id (engine/item-id->element-id file-id :file-storage)]
       [{:icon     :drive_file_rename_outline
         :on-click [:file-storage/edit-rendered-file-alias! file-id]
         :tooltip  :rename-file!}
        {:icon     :delete_outline
         :on-click [:file-storage/render-delete-rendered-file-dialog! file-id]
         :tooltip  :delete-file!}
        {:icon     :more_vert
         :on-click [:elements/render-context-surface! element-id]
         :tooltip  :more-options}]))

(defn- path-param->file-storage-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) path-param
  ;
  ; @return (string)
  [path-param]
  (str "/media/" path-param))

(defn directory-id->file-storage-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @example
  ;  (directory-id->file-storage-uri :my-directory)
  ;  => "/file-storage/my-directory"
  ;
  ; @return (string)
  [directory-id]
  (let [directory-id (name directory-id)]
       (path-param->file-storage-uri directory-id)))

(defn- view-props->capacity-indicator-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:storage-total-capacity (B)
  ;   :storage-used-capacity (B)}
  ;
  ; @return (string)
  [{:keys [storage-total-capacity storage-used-capacity]}]
  (let [storage-total-capacity (io/B->GB storage-total-capacity)
        storage-used-capacity  (io/B->GB storage-used-capacity)]
       (str storage-used-capacity " GB / " storage-total-capacity " GB")))



;; -- Current route directory subscriptions -----------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-route-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (if-let [current-directory-id (r router/get-current-route-path-param db :directory-id)]
          (keyword current-directory-id)
          (return  engine/ROOT-DIRECTORY-ID)))



;; -- Rendered directory subscriptions ----------------------------------------
;; ----------------------------------------------------------------------------

(defn get-rendered-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [namespace (a/get-namespace ::this)]
       (r engine/get-rendered-directory-id db namespace)))

(defn get-rendered-file-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (keyword) prop-id
  ;
  ; @return (string)
  [db [_ file-id prop-id]]
  (let [rendered-directory-id (r get-rendered-directory-id db)]
       (r engine/get-file-prop db rendered-directory-id file-id prop-id)))

(a/reg-sub :file-storage/get-rendered-file-prop get-rendered-file-prop)

(defn get-rendered-subdirectory-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  ; @param (keyword) prop-id
  ;
  ; @return (string)
  [db [_ subdirectory-id prop-id]]
  (let [rendered-directory-id (r get-rendered-directory-id db)]
       (r engine/get-subdirectory-prop db rendered-directory-id subdirectory-id prop-id)))

(a/reg-sub :file-storage/get-rendered-subdirectory-prop get-rendered-subdirectory-prop)



;; -- View subscriptions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-surface-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db]
  (str (r a/get-app-detail   db :app-title)
       (param " ")
       (r dictionary/look-up db :file-storage)))

(defn- get-order-by
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (get-in db (settings-item-path :order-by)
             (param DEFAULT-ORDER-BY)))

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:directory-exists? (boolean)
  ;   :directory-alias (string)
  ;   :directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)
  ;   :directory-empty? (boolean)
  ;   :directory-path (maps in vector)
  ;   :filtered-files (maps in vector)
  ;   :filtered-subdirectories (maps in vector)
  ;   :order-by (keyword)
  ;   :storage-free-capacity (B)
  ;   :storage-total-capacity (B)
  ;   :storage-used-capacity (B)
  ;   :synchronizing? (boolean)}
  [db _]
  (let [namespace               (a/get-namespace ::this)
        query-id                (engine/namespace->query-id namespace)
        rendered-directory-id   (r get-rendered-directory-id db)
        filter-phrase           (r elements/get-field-value           db ::filter-items-field)
        filtered-files          (r engine/get-filtered-files          db rendered-directory-id filter-phrase)
        filtered-subdirectories (r engine/get-filtered-subdirectories db rendered-directory-id filter-phrase)]
       {:directory-exists?                (r engine/directory-exists?          db rendered-directory-id)
        :directory-alias                  (r engine/get-directory-alias        db rendered-directory-id)
        :directory-empty?                 (r engine/directory-empty?           db rendered-directory-id)
        :directory-path                   (r engine/get-directory-path         db rendered-directory-id)
        :order-by                         (r get-order-by                      db)

        :synchronizing?                   (r sync/listening-to-request?        db query-id)
        :directory-render-files?          (map/nonempty? filtered-files)
        :directory-render-subdirectories? (map/nonempty? filtered-subdirectories)
        :filtered-files                   (param filtered-files)
        :filtered-subdirectories          (param filtered-subdirectories)}))

(a/reg-sub :file-storage/get-body-props get-body-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _])
;  (merge (r pattern/get-item-list-header-view-props    db "media")))
;         (r pattern/get-item-browser-header-view-props db "media")))

(a/reg-sub ::get-header-props get-header-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:storage-free-capacity            (r engine/get-storage-free-capacity  db)
   :storage-total-capacity           (r engine/get-storage-total-capacity db)
   :storage-used-capacity            (r engine/get-storage-used-capacity  db)})

(a/reg-sub :file-storage/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-storage-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) storage-id
  ; @param (map) storage-props
  ;
  ; @return (map)
  [db [_ storage-id storage-props]]
  (update-in db (settings-item-path) merge storage-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-storage/go-home!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:elements/empty-field! ::filter-items-field]
                [:router/go-to! "/media"]]})

(a/reg-event-fx
  :file-storage/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [namespace           (a/get-namespace ::this)
            parent-directory-id (r engine/get-parent-directory-id db namespace)
            file-storage-uri    (directory-id->file-storage-uri parent-directory-id)]
           {:dispatch-n [[:elements/empty-field! ::filter-items-field]
                         [:router/go-to! file-storage-uri]]})))

(a/reg-event-fx
  :file-storage/go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [_ [_ subdirectory-id]]
      (let [file-storage-uri (directory-id->file-storage-uri subdirectory-id)]
           {:dispatch-n [[:elements/empty-field! ::filter-items-field]
                         [:router/go-to! file-storage-uri]]})))

(a/reg-event-fx
  :file-storage/render-order-by-select!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:elements/render-select-options! ::order-by-select-options
                                    {:default-value DEFAULT-ORDER-BY
                                     :options-label :order-by
                                     :options [{:label :by-date :value :by-date}
                                               {:label :by-name :value :by-name}
                                               {:label :by-size :value :by-size}]
                                     :value-path (settings-item-path :order-by)}])

(a/reg-event-fx
  :file-storage/set-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [rendered-directory-id (r get-rendered-directory-id  db)]
           (if (r engine/directory-exists? db rendered-directory-id)
               (let [rendered-directory-alias (r engine/get-directory-alias db rendered-directory-id)]
                    {:dispatch-n [[:ui/set-header-title! rendered-directory-alias]
                                  [:ui/set-window-title! rendered-directory-alias]]})))))

(a/reg-event-fx
  :file-storage/edit-rendered-file-alias!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [namespace             (a/get-namespace ::this)
            rendered-directory-id (r get-rendered-directory-id db)
            file-alias            (r get-rendered-file-prop    db file-id :file/alias)
            action-id             (engine/namespace->query-id namespace)
            action-props          {:source-directory-id rendered-directory-id
                                   :file-id             file-id}]
           [:value-editor/edit!
            :media/alias-editor
            {:initial-value file-alias
             :label         :filename
             :on-save       [:media/->file-alias-edited action-id action-props]
             :validator     {:f io/filename-valid?
                             :invalid-message :invalid-filename
                             :pre-validate? true}}])))

(a/reg-event-fx
  :file-storage/edit-rendered-subdirectory-alias!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [namespace             (a/get-namespace ::this)
            rendered-directory-id (r get-rendered-directory-id      db)
            subdirectory-alias    (r get-rendered-subdirectory-prop db subdirectory-id :directory/alias)
            action-id             (engine/namespace->query-id namespace)
            action-props          {:source-directory-id rendered-directory-id
                                   :subdirectory-id     subdirectory-id}]
           [:value-editor/edit!
            :media/alias-editor
            {:initial-value subdirectory-alias
             :label         :directory-name
             :on-save       [:media/->subdirectory-alias-edited action-id action-props]
             :validator     {:f io/directory-name-valid?
                             :invalid-message :invalid-directory-name
                             :pre-validate? true}}])))

(a/reg-event-fx
  :file-storage/delete-rendered-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) action-props
  ;  {:selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-props]]
      (let [namespace             (a/get-namespace ::this)
            rendered-directory-id (r get-rendered-directory-id db)
            action-id             (engine/namespace->query-id namespace)
            action-props          (assoc action-props :source-directory-id rendered-directory-id)]
           [:media/delete-item! action-id action-props])))

(a/reg-event-fx
  :file-storage/copy-rendered-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) action-props
  ;  {:selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-props]]
      (let [namespace                (a/get-namespace ::this)
            destination-directory-id (get-in db (settings-item-path :destination-directory-id))
            action-id                (engine/namespace->query-id namespace)
            action-props             (assoc action-props :destination-directory-id destination-directory-id)]
           [:media/copy-item! action-id action-props])))

(a/reg-event-fx
  :file-storage/move-rendered-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) action-props
  ;  {:selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-props]]
      (let [namespace                (a/get-namespace ::this)
            destination-directory-id (get-in db (settings-item-path :destination-directory-id))
            source-directory-id      (r get-rendered-directory-id db)
            action-id                (engine/namespace->query-id namespace)
            action-props             (assoc action-props :destination-directory-id destination-directory-id
                                                         :source-directory-id      source-directory-id)]
           [:media/move-item! action-id action-props])))

(a/reg-event-fx
  :file-storage/save-rendered-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r get-rendered-directory-id db)
            file-alias            (r engine/get-file-prop db rendered-directory-id file-id :file/alias)
            filename              (r engine/get-file-prop db rendered-directory-id file-id :file/filename)
            uri                   (media/filename->media-storage-uri filename)]
           [:tools/save-file! {:filename file-alias :uri uri}])))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-storage/->directory-data-downloaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  (fn [{:keys [db]} [_ directory-id]]
      (let [namespace (a/get-namespace ::this)]
           {:db (r engine/set-rendered-directory-id! db namespace directory-id)
            :dispatch [:file-storage/set-title!]})))



;; -- Control bar components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-storage-filter-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)
  ;   :synchronizing? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty? synchronizing?]}]
  (let [field-disabled? (or directory-empty? synchronizing?)]
       [elements/text-field ::filter-items-field
                            {:auto-focus? true
                             :disabled?   field-disabled?
                             :emptiable?  true
                             :placeholder :filter-items!}]))

(defn- file-storage-bin-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::bin-button
                   {:color    :default
                    :icon     :delete_outline
                    :layout   :icon-button
                    :on-click [:router/go-to! (path-param->file-storage-uri "bin")]
                    :variant  :transparent}])

(defn- file-storage-directory-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-exists? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-exists?]}]
  (let [namespace (a/get-namespace ::this)]
       [:<> [directory-actions/file-upload-button      component-id
                                                       {:disabled? (not   directory-exists?)
                                                        :namespace (param namespace)}]
            [directory-actions/create-directory-button component-id
                                                       {:disabled? (not   directory-exists?)
                                                        :namespace (param namespace)}]]))

(defn file-storage-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [elements/polarity ::control-bar
                     {:start-content [:<> [file-storage-directory-actions component-id view-props]
                                         ;[elements/vertical-line {:color :default :layout :row}]
                                          [elements/vertical-separator {:size :m}]
                                          [file-storage-filter-items-field component-id view-props]]
                      :end-content   [:<> [elements/vertical-separator {:size :m}]
                                          ;[file-storage-capacity-indicator component-id view-props]
                                          [elements/vertical-separator {:size :m}]]}])
                                         ;[file-storage-bin-button component-id view-props]
                                         ;[elements/vertical-separator {:size :m}]



;; -- Directory label bar components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-storage-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ {:keys [directory-exists?] :as view-props}]
  [elements/button ::home-button
                   {:color     :default
                    :disabled? (engine/view-props->root-level? view-props)
                    :icon      :home
                    :layout    :icon-button
                    :on-click  [:file-storage/go-home!]
                    ;:tooltip   :my-storage
                    :variant   :transparent}])
                    ;:width     :fit}])

(defn file-storage-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ view-props]
  [elements/button ::up-button
                   {:disabled? (engine/view-props->root-level? view-props)
                    :icon      :chevron_left
                    :layout    :icon-button
                    :on-click  [:file-storage/go-up!]
                    ;:tooltip   :back!
                    :variant   :transparent}])
                    ;:width     :fit}])

(defn file-storage-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-alias (string)}
  ;
  ; @return (component)
  [_ {:keys [directory-alias]}]
  [elements/label ::label
                  {:content directory-alias :horizontal-align :left}])

(defn file-storage-order-by-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty? synchronizing?]}]
  [elements/button ::order-by-select-button
                   {:color     :default
                    :disabled? (or directory-empty? synchronizing?)
                    :icon      :sort
                    :tooltip   :order-by
                    :layout    :icon-button
                    :variant   :transparent
                    :on-click  [:file-storage/render-order-by-select!]}])

(defn file-storage-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [elements/polarity ::label-bar
                     {:start-content [:<> [file-storage-home-button     component-id view-props]
                                          [file-storage-up-button       component-id view-props]]
                                          ;[elements/vertical-separator {:size :s}]
                                          ;[file-storage-label           component-id view-props]]
                      :end-content   [:<> [file-storage-filter-items-field component-id view-props]
                                          [elements/vertical-separator {:size :xxs}]
                                          [file-storage-directory-actions component-id view-props]
                                          ;[file-storage-filter-items-field component-id view-props]

                                          [file-storage-order-by-select    component-id view-props]]}])

(defn- file-storage-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [file-storage-label-bar component-id view-props]])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-storage-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/label {:content (view-props->capacity-indicator-label view-props)
                   :color :muted :font-size :xxs :layout :fit}])

(defn- file-storage-subdirectory
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;  {:directory/alias (metamorphic-value)
  ;   :directory/content-size (B)
  ;   :directory/item-count (integer)}
  ;
  ; @return (component)
  [component-id view-props subdirectory-id {:directory/keys [alias content-size item-count]
                                            :as subdirectory-props}]
  (let [element-id           (engine/item-id->element-id subdirectory-id :file-storage)
        stickers             (file-storage-subdirectory-stickers component-id view-props subdirectory-id subdirectory-props)
        on-click-event       [:file-storage/go-to! subdirectory-id]
        on-right-click-event [:elements/render-context-surface! element-id]]
       [elements/directory element-id
                           {:content-size   content-size
                            :item-count     item-count
                            :label          alias
                            :on-click       on-click-event
                            :on-right-click on-right-click-event
                            :stickers       stickers
                            :context-surface {:content      #'context-menu/file-storage-subdirectory-context-menu
                                              :content-props subdirectory-props}}]))

(defn- file-storage-subdirectory-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:filtered-subdirectories (maps in vector)}
  ;
  ; @return (component)
  [component-id {:keys [filtered-subdirectories] :as view-props}]
  (reduce (fn [directory-list subdirectory-id]
              (let [subdirectory-props (get filtered-subdirectories subdirectory-id)]
                   (vector/conj-item directory-list ^{:key subdirectory-id}
                                     [file-storage-subdirectory component-id
                                      view-props subdirectory-id subdirectory-props])))
          [:<>]
          (engine/view-props->ordered-subdirectories view-props)))

(defn- file-storage-subdirectories
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-render-files? directory-render-subdirectories?] :as view-props}]
  (if (boolean directory-render-subdirectories?)
      [:<> [elements/row ::subdirectories
                         {:content [file-storage-subdirectory-list component-id view-props]
                          :wrap-items? true}]
           (if directory-render-files?
               [:<> [elements/horizontal-separator {:size :s}]
                    [elements/horizontal-line {:color :highlight}]
                    [elements/horizontal-separator {:size :s}]])]))

(defn- file-storage-file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/alias (metamorphic-value)
  ;   :file/filesize (string)}
  ;
  ; @return (component)
  [component-id view-props file-id {:file/keys [alias filesize modified-at] :as file-props}]
  (let [element-id           (engine/item-id->element-id file-id :file-storage)
        stickers             (file-storage-file-stickers component-id view-props file-id file-props)
        timestamp            (time/timestamp-string->date-and-time modified-at :yyyymmdd :hhmm)
        on-right-click-event [:elements/render-context-surface! element-id]
        thumbnail-uri        (engine/file-props->thumbnail-uri file-props)]
       [elements/file element-id
                      {:filesize       filesize
                       :label          alias
                       :on-right-click on-right-click-event
                       :stickers       stickers
                       :timestamp      timestamp
                       :thumbnail-uri  thumbnail-uri
                       :context-surface {:content       #'context-menu/file-storage-file-context-menu
                                         :content-props file-props}}]))

(defn- file-storage-file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:filtered-files (maps in vector)}
  ;
  ; @return (component)
  [component-id {:keys [filtered-files] :as view-props}]
  (reduce (fn [file-list file-id]
              (let [file-props (get filtered-files file-id)]
                   (vector/conj-item file-list ^{:key file-id}
                                     [file-storage-file component-id view-props file-id file-props])))
          [:<>]
          (engine/view-props->ordered-files view-props)))

(defn- file-storage-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-render-files? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-render-files?] :as view-props}]
  (if (boolean directory-render-files?)
      [elements/row ::files
                    {:content [file-storage-file-list component-id view-props]
                     :wrap-items? true}]))

(defn- file-storage-file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
 ;[elements/file-drop-area ::file-drop-area
 ;                         {:font-size :xs}]
  [elements/label {:content :empty-directory :color :highlight}])

(defn- file-storage-directory-empty
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-empty? directory-exists?] :as view-props}]
  (if (and directory-empty? directory-exists?)
      [:<> [elements/horizontal-separator {:size :s}]
           [elements/polarity ::directory-empty
                              {:middle-content [file-storage-file-drop-area component-id view-props]}]
           [elements/horizontal-separator {:size :s}]]))

(defn- file-storage-directory-not-exists
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-exists? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-exists?] :as view-props}]
  (if (not directory-exists?)
      [:<> [elements/horizontal-separator {:size :s}]
           [elements/polarity ::directory-not-exists
                              {:middle-content [elements/label {:content :directory-does-not-exists}]}]
           [elements/horizontal-separator {:size :s}]]))

(defn- file-storage-no-filtered-items-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)
  ;   :directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty? directory-render-files? directory-render-subdirectories?]}]
  (if (and (not directory-render-files?)
           (not directory-render-subdirectories?)
           (not directory-empty?))
      [elements/polarity ::no-filtered-items-match
                         {:middle-content [elements/label {:content :no-items-found :color :highlight}]}]))

(defn- file-storage-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [file-storage-label-bar               component-id view-props]
       [file-storage-subdirectories          component-id view-props]
       [file-storage-files                   component-id view-props]
       [file-storage-directory-empty         component-id view-props]
       [file-storage-directory-not-exists    component-id view-props]
       [file-storage-no-filtered-items-match component-id view-props]
       [elements/horizontal-separator {:size :xs}]])



;; -- Media item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id item-dex item-props common-props]
  [:div "item"])



;; -- Directory-browser header components -------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-browser-mobile-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props])
;  [elements/polarity ::desktop-header])
;                     {:start-content [:<> [pattern/item-list-new-item-select    "media" {:options [:create-directory! :upload-files!]}]]}])
;                                          [pattern/item-browser-home-button     "media" view-props]]}])
;                                          [pattern/item-browser-up-button       "media" view-props]]}])
;                                          [pattern/item-list-sort-items-button  "media" {:options [:by-name :by-date]}]]}])
;                      :end-content   [:<> [pattern/item-list-search-mode-button "media"]]}])

(defn- item-browser-desktop-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props])
;  [elements/polarity ::desktop-header])
;                     {:start-content [:<> [pattern/item-list-new-item-select    "media" {:options [:create-directory! :upload-files!]}]]}])
;                                          [pattern/item-browser-home-button     "media" view-props]]}])
;                                          [pattern/item-browser-up-button       "media" view-props]]}])
;                                          [pattern/item-list-sort-items-button  "media" {:options [:by-name :by-date]}]]}])
;                      :end-content   [:<> [pattern/item-list-search-items-field "media"]]}])

(defn- item-browser-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [search-mode? viewport-small?] :as view-props}]
  (cond ; search-mode & small viewport
;        (and viewport-small? search-mode?)
;        [pattern/item-list-search-header "media"]
        ; small viewport
        (boolean viewport-small?)
        [item-browser-mobile-header  surface-id view-props]
        ; large viewport
        :desktop-header
        [item-browser-desktop-header surface-id view-props]))



;; -- Directory-browser body components ---------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-browser-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-lister/body :media {:on-list-ended [:extensions/request-browser-items! "media" "directory"
                                                                               {:base-query  engine/ROOT-DIRECTORY-QUERY
                                                                                :item-params engine/DOWNLOAD-DIRECTORY-DATA-PARAMS}]
                            :element       #'media-item
                            :request-id    :extensions/request-browser-items!
                            ;:sortable?     true
                            ;:subscriber    [::get-common-props]
                            :value-path    [:media :browser-data]}])



;; -- Directory-browser components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a surface-id {:body   {:content    #'item-browser-body}
                                :header {:content    #'item-browser-header
                                         :subscriber [::get-header-view-props]}
                                :label       "Saját tárhely"
                                :description (components/content {:content :npn-items-downloaded
                                                                  :replacements [10 104]})}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :media/render-directory-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view}}])



; WARNING! DEPRECATED?
(a/reg-event-fx
  :file-storage/render-rendered-subdirectory-properties!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [rendered-directory-id (r get-rendered-directory-id db)]
           [:media/render-subdirectory-properties! rendered-directory-id subdirectory-id])))
; WARNING! DEPRECATED?

; WARNING! DEPRECATED?
(a/reg-event-fx
  :file-storage/render-rendered-file-properties!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r get-rendered-directory-id db)]
           [:media/render-file-properties! rendered-directory-id file-id])))
; WARNING! DEPRECATED?



;(a/reg-lifecycles)
;  ::lifecycles)
;  {:on-app-init [:item-browser/add-routes! "media" "directory"]}) ;{:default-item-id "home"}]})
