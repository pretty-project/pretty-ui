
(ns extensions.pattern
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.map        :refer [dissoc-in]]
              [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-router.api      :as router]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-id->new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) item-id
  ; @param (string) item-name
  ;
  ; @example
  ;  (item-id->new-item? "new-product" "product")
  ;  =>
  ; true
  ;
  ; @example
  ;  (item-id->new-item? "my-product" "product")
  ;  =>
  ; false
  ;
  ; @return (boolean)
  [item-id item-name]
  (= item-id (str "new-" item-name)))

(defn- item-id->form-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) item-id
  ; @param (string) item-name
  ;
  ; @example
  ;  (item-id->form-label "new-product" "product")
  ;  =>
  ; :add-product
  ;
  ; @example
  ;  (item-id->form-label "my-product" "product")
  ;  =>
  ; :edit-product
  ;
  ; @return (metamorphic-content)
  [item-id item-name]
  (if (item-id->new-item? item-id item-name)
      (keyword/join "add-"  item-name)
      (keyword/join "edit-" item-name)))

(defn item-id->item-uri
  ; @param (string) extension-name
  ; @param (string) item-id
  ;
  ; @example
  ;  (pattern/item-id->item-uri "products" "my-product")
  ;  =>
  ;  "/products/my-product"
  ;
  ; @return (string)
  [extension-name item-id]
  (str "/" extension-name "/" item-id))



;; -- Item-form subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item?
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  (r pattern/new-item? db "products" "product")
  ;
  ; @return (boolean)
  [db [_ extension-name item-name]]
  (let [extension-id (keyword extension-name)
        item-id-key  (keyword/join item-name "-id")
        item-id (get-in db [extension-id :form-meta item-id-key])]
       (item-id->new-item? item-id item-name)))



;; -- Item-browser subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-browser-current-path
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/get-item-browser-current-path db "media")
  ;
  ; @return (vector)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)
        current-path (get-in db [extension-id :browser-meta :current-path])]
       (vec current-path)))

(defn item-browser-at-home?
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/item-browser-at-home? db "media")
  ;
  ; @return (boolean)
  [db [_ extension-name]]
  (let [current-path (r get-item-browser-current-path db extension-name)]
       (empty? current-path)))

(defn get-item-browser-header-view-props
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/get-item-browser-header-view-props db "media")
  ;
  ; @return (map)
  ;  {:at-home? (boolean)
  ;   :search-mode? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       {:at-home?        (r item-browser-at-home? db extension-name)
        :search-mode?    (get-in db [extension-id :browser-meta :search-mode?])
        :viewport-small? (r environment/viewport-small? db)}))



;; -- Item-list subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-list-header-view-props
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/get-item-list-header-view-props db "products")
  ;
  ; @return (map)
  ;  {:viewport-small? (boolean)}
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       {:search-mode?    (get-in db [extension-id :list-meta :search-mode?])
        :viewport-small? (r environment/viewport-small? db)}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-item-browser-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/toggle-item-browser-search-mode! "media")
  ;
  ; @return (map)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (update-in db [extension-id :browser-meta :search-mode?] not)))

(a/reg-event-db :extensions/toggle-item-browser-search-mode! toggle-item-browser-search-mode!)

(defn- toggle-item-list-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/toggle-item-list-search-mode! "products")
  ;
  ; @return (map)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (update-in db [extension-id :list-meta :search-mode?] not)))

(a/reg-event-db :extensions/toggle-item-list-search-mode! toggle-item-list-search-mode!)

(defn- toggle-item-browser-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/toggle-item-browser-select-mode! "media")
  ;
  ; @return (map)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (update-in db [extension-id :browser-meta :select-mode?] not)))

(a/reg-event-db :extensions/toggle-item-browser-select-mode! toggle-item-browser-select-mode!)

(defn- toggle-item-list-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/toggle-item-list-select-mode! "products")
  ;
  ; @return (map)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (update-in db [extension-id :list-meta :select-mode?] not)))

(a/reg-event-db :extensions/toggle-item-list-select-mode! toggle-item-list-select-mode!)



;; -- Item list pattern events ------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions/load-item-list!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/load-item-list! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id   (keyword extension-name (str "synchronize-" item-name "-list!"))
            render-event (keyword extension-name (str "render-"      item-name "-list!"))
            extension-id (keyword extension-name)]
           {:db         (dissoc-in db [extension-id :list-data])
                        ;[:x.app-ui/listen-to-process! :product/synchronize-product-list!]
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                        ;[:x.app-ui/set-header-title!  :products]
                         [:x.app-ui/set-header-title!  extension-id]
                        ;[:x.app-ui/set-window-title!  :products]
                         [:x.app-ui/set-window-title!  extension-id]
                        ;[:products/render-product-list!]
                         [render-event]]})))

(a/reg-event-fx
  :extensions/add-item-list-route!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/add-item-list-route! "products" "product"]
  (fn [_ [_ extension-name item-name]]
           ;route-id :products/list-route
      (let [route-id (keyword extension-name "list-route")]
           [:x.app-router/add-route! route-id
                                     ;:route-template "/products"
                                     {:route-template (str "/" extension-name)
                                     ;:route-event    [:extensions/load-item-list! "products" "product"]
                                      :route-event    [:extensions/load-item-list! extension-name item-name]
                                      :restricted?    true}])))



;; -- Item form pattern events ------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions/load-item-form!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/load-item-form! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [extension-id (keyword extension-name)
            item-id-key  (keyword/join item-name "-id")
            item-id      (r router/get-current-route-path-param db item-id-key)
            new-item?    (item-id->new-item?  item-id item-name)
            header-label (item-id->form-label item-id item-name)
            request-id   (keyword extension-name (str "synchronize-" item-name "-form!"))
            request-item-event (keyword extension-name (str "request-" item-name "!"))
            render-event       (keyword extension-name (str "render-"  item-name "-form!"))]
                              ;(assoc-in  [:products :form-meta :product-id] "my-product")
           {:db         (-> db (assoc-in  [extension-id :form-meta item-id-key] item-id)
                              ;(dissoc-in [:products :form-data])
                               (dissoc-in [extension-id :form-data]))
                        ;[:x.app-ui/listen-to-process! :products/synchronize-product-form!]
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                        ;[:x.app-ui/set-header-title!  :edit-product]
                         [:x.app-ui/set-header-title!  header-label]
                        ;[:x.app-ui/set-header-title!  :edit-product]
                         [:x.app-ui/set-window-title!  header-label]
                                          ;[:products/request-product!]
                         (if-not new-item? [request-item-event item-id])
                        ;[:product/render-form!]
                         [render-event]]})))

(a/reg-event-fx
  :extensions/add-item-form-route!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/add-item-form-route! "products" "product"]
  (fn [_ [_ extension-name item-name]]
           ;route-id :products/form-route
      (let [route-id (keyword extension-name "form-route")]
           [:x.app-router/add-route! route-id
                                     ;:route-template "/products/:product-id"
                                     {:route-template (str "/" extension-name "/:" item-name "-id")
                                     ;:route-parent   "/products"
                                      :route-parent   (str "/" extension-name)
                                     ;:route-event    [:extensions/load-item-form! "products" "product"]
                                      :route-event    [:extensions/load-item-form! extension-name item-name]
                                      :restricted?    true}])))



;; -- Item browser pattern events ---------------------------------------------
;; ----------------------------------------------------------------------------



; WARNING!

; subbrowser-ként fog menni a file-browser
;
; Re-Frame DB:
; {:media {:browser-data [] (ez közös)
;          :browser-meta {}
;          :subbrowser-meta {}}}

; WARNING!



(a/reg-event-fx
  :extensions/load-item-browser!
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) browser-props
  ;  {:default-item-id (string)(opt)}
  ;
  ; @usage
  ;  [:extensions/load-item-browser! "media" "directory" {:default-item-id "home"}]
  (fn [{:keys [db]} [_ extension-name item-name {:keys [default-item-id]}]]
      (let [render-event (keyword extension-name (str "render-" item-name "-browser!"))
            extension-id (keyword extension-name)
            item-id-key  (keyword/join item-name "-id")
            item-id      (or (r router/get-current-route-path-param db item-id-key)
                             (param default-item-id))]
                              ;(assoc-in  [:media :browser-meta :directory-id] "my-directory")
           {:db         (-> db (assoc-in  [extension-id :browser-meta item-id-key] item-id)
                              ;(dissoc-in [:meda :browser-data])
                               (dissoc-in [extension-id :browser-data]))
            :dispatch-n [[:x.app-ui/listen-to-process! :extensions/request-browser-items!]
                        ;[:x.app-ui/set-header-title!  :media]
                         [:x.app-ui/set-header-title!  extension-id]
                        ;[:x.app-ui/set-window-title!  :media]
                         [:x.app-ui/set-window-title!  extension-id]
                        ;[:media/render-directory-browser!]
                         [render-event]]})))

(a/reg-event-fx
  :extensions/add-item-browser-routes!
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) browser-props
  ;
  ; @usage
  ;  [:extensions/add-item-browser-routes! "media" "directory"]
  (fn [_ [_ extension-name item-name browser-props]]
           ;route-id          :media/route
      (let [route-id          (keyword extension-name "route")
           ;extended-route-id :media/extended-route
            extended-route-id (keyword extension-name "extended-route")]
           {:dispatch-n [[:x.app-router/add-route! route-id
                                                   {:route-event    [:extensions/load-item-browser! extension-name item-name browser-props]
                                                   ;:route-template "/media"
                                                    :route-template (str "/" extension-name)
                                                    :restricted?    true}]
                         [:x.app-router/add-route! extended-route-id
                                                   {:route-event    [:extensions/load-item-browser! extension-name item-name browser-props]
                                                   ;:route-template "/media/:directory-id"
                                                    :route-template (str "/" extension-name "/:" item-name "-id")
                                                    :restricted?    true}]]})))



;; -- Item-list search components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-quit-search-mode-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-list-quit-search-mode-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::quit-search-mode-button
                   {:on-click [:extensions/toggle-item-list-search-mode! extension-name]
                    :preset   :close-icon-button}])

(defn- item-list-search-mode-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-list-search-mode-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::search-button
                   {:on-click [:extensions/toggle-item-list-search-mode! extension-name]
                    :preset   :search-icon-button
                    :tooltip  :search}])

(defn- item-list-search-items-field
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-list-search-items-field "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/search-field ::search-field
                         {:auto-focus? true
                          :layout      :row
                          :min-width   :xs
                          :placeholder :search}])

(defn- item-list-search-header
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-list-search-header "products"]
  ;
  ; @return (component)
  [extension-name]
  [:div.x-search-bar-a [:div.x-search-bar-a--search-field
                         [item-list-search-items-field extension-name]]
                       [item-list-quit-search-mode-button extension-name]])



;; -- Item-list action components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-list-add-new-item-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [pattern/item-list-add-new-item-button "products" "product"]
  ;
  ; @return (component)
  [extension-name item-name]
  [elements/button ::add-new-list-item-button
                   ;:on-click [:x.app-router/go-to! "/products/new-product"]
                   {:on-click [:x.app-router/go-to! (str "/" extension-name "/new-" item-name)]
                    :preset   :add-icon-button
                    :tooltip  :add-new!}])

(defn- item-list-select-multiple-items-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-list-select-multiple-items-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::select-multiple-list-item-button
                   {:on-click [:extensions/toggle-item-list-select-mode! extension-name]
                    :preset  :select-more-icon-button
                    :tooltip :check}])

(defn- item-list-delete-selected-items-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-list-delete-selected-items-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::delete-selected-button
                   {:on-click [:extensions/delete-item-list-selected-items! extension-name]
                    :preset   :delete-icon-button
                    :tooltip  :delete!}])

(defn- item-list-sort-items-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:options (vector)}
  ;
  ; @usage
  ;  [pattern/item-list-sort-items-button "products" {:options [:by-name :by-date]}]
  ;
  ; @return (component)
  [extension-name {:keys [options]}]
  (let [extension-id (keyword extension-name)]
       [elements/select ::sort-items-button
                        {:as-button?    true
                         :options-label :order-by
                         :preset        :sort-by-icon-button
                         :tooltip       :sort-by
                         :initial-options options
                         :value-path      [extension-id :list-meta :sort-by]}]))



;; -- Item-list search components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-browser-quit-search-mode-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-browser-quit-search-mode-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::quit-search-mode-button
                   {:on-click [:extensions/toggle-item-browser-search-mode! extension-name]
                    :preset   :close-icon-button}])

(defn item-browser-search-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-browser-home-button "media"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::search-button
                   {:on-click [:extensions/toggle-item-browser-search-mode! extension-name]
                    :preset   :search-icon-button
                    :tooltip  :search}])

(defn- item-browser-search-field
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-browser-search-field "media"]
  ;
  ; @return (component)
  [extension-name]
  [elements/search-field ::search-field
                         {:auto-focus? true
                          :layout      :row
                          :min-width   :xs
                          :placeholder :search}])

(defn- item-browser-search-header
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [pattern/item-browser-search-header "media"]
  ;
  ; @return (component)
  [extension-name]
  [:div.x-search-bar-a [:div.x-search-bar-a--search-field
                         [item-browser-search-field extension-name]]
                       [item-browser-quit-search-mode-button extension-name]])



;; -- Item-browser action components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-browser-new-item-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:options (vector)}
  ;
  ; @usage
  ;  [pattern/item-browser-new-item-button "media" {:options [:create-directory! :upload-files!]}]
  ;
  ; @return (component)
  [extension-name {:keys [options]}]
  [elements/select ::new-item-button
                   {:as-button?      true
                    :autoclear?      true
                    :initial-options options
                   ;:on-select       [:media/add-new-item!]
                    :on-select       [(keyword extension-name "add-new-item!")]
                    :preset          :add-icon-button}])

(defn item-browser-home-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @usage
  ;  [pattern/item-browser-home-button "media" {:at-home? false}]
  ;
  ; @return (component)
  [extension-name {:keys [at-home?]}]
  [elements/button ::home-button
                   ;:on-click  [:media/go-home!]
                   {:on-click  [(keyword extension-name "go-home!")]
                    :disabled? at-home?
                    :preset    :home-icon-button}])

(defn item-browser-up-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @usage
  ;  [pattern/item-browser-up-button "media" {:at-home? false}]
  ;
  ; @return (component)
  [extension-name {:keys [at-home?]}]
  [elements/button ::up-button
                   {:disabled? at-home?
                   ;:on-click  [:media/go-up!]
                    :on-click  [(keyword extension-name "go-up!")]
                    :preset    :up-icon-button}])

(defn- item-browser-sort-items-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:options (vector)}
  ;
  ; @usage
  ;  [pattern/item-browser-sort-items-button "products" {:options [:by-name :by-date]}]
  ;
  ; @return (component)
  [extension-name {:keys [options]}]
  (let [extension-id (keyword extension-name)]
       [elements/select ::sort-items-button
                        {:as-button?    true
                         :options-label :order-by
                         :preset        :sort-by-icon-button
                         :tooltip       :sort-by
                         :initial-options options
                         :value-path      [extension-id :browser-meta :sort-by]}]))
