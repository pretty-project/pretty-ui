
(ns x.app-developer.docs
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.css    :as css]
              [mid-fruits.io     :as io]
              [mid-fruits.map    :as map]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- namespace-data->namespace-index
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [namespace-data]
  (let [namespace-list (map/get-keys namespace-data)]
                ; Üres vagy sérült forrásfájlok ...
       (reduce #(if-let [namespace (get-in namespace-data [%2 :docs :namespace])]
                        (let [extension       (io/filepath->extension %2)
                              namespace       (get-in namespace-data [%2 :docs :namespace])
                              namespace-parts (string/split namespace ".")]
                             (assoc-in %1 (vector/cons-item namespace-parts extension) %2))
                        (return %1))
                (param {})
                (param namespace-list))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scopes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [meta]}]
  [:div {:style {:padding "12px 12px"}}
       [:div {:style {:padding-bottom "8px" :opacity ".6"}} "Scopes"]
       [:div {:on-click #(a/dispatch [:db/set-item! [:docs :meta :scope] "clj"])
              :style {:font-size "13px" :line-height "22px" :cursor "pointer" :opacity (if (not= (:scope meta) "clj") ".6")}}
             "CLJ"]
       [:div {:on-click #(a/dispatch [:db/set-item! [:docs :meta :scope] "cljc"])
              :style {:font-size "13px" :line-height "22px" :cursor "pointer" :opacity (if (not= (:scope meta) "cljc") ".6")}}
             "CLJC"]
       [:div {:on-click #(a/dispatch [:db/set-item! [:docs :meta :scope] "cljs"])
              :style { :font-size "13px" :line-height "22px" :cursor "pointer" :opacity (if (not= (:scope meta) "cljs") ".6")}}
             "CLJS"]])

(defn- namespace-tree
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [meta] :as view-props} namespace-index path]
       ;name-list ["my-module" "your-module" "our-namespace.clj"]
  (let [name-list (map/get-keys namespace-index)
       ;name-list ["my-module" "our-namespace.clj" "your-module"]
        name-list (vector/abc   name-list)]
       (reduce (fn [tree name]
                   (vector/conj-item tree
                                     (let [depth     (dec (count path))
                                           path      (vector/conj-item path name)]
                                          [:div {:style {:font-size "13px" :line-height "22px" :padding-left (css/px (if (> depth 0) 10)) :cursor "pointer"}}
                                                    ; If name points to a module (directory) ...
                                                (if (map? (get namespace-index name))
                                                    (let [expanded? (get-in meta [path :expanded?])]
                                                         [:<> [:div {:on-click #(a/dispatch [:db/apply! [:docs :meta path :expanded?] not])
                                                                     :style {:opacity ".6"}}
                                                                    (str name)]
                                                              (if expanded? [namespace-tree surface-id view-props (get namespace-index name) path])])
                                                    ; If name points to a namespace (file) ...
                                                    (let [namespace (get namespace-index name)
                                                          selected? (= namespace (:namespace meta))]
                                                         [:div {:on-click #(a/dispatch [:db/set-item! [:docs :meta :namespace] namespace]) :style {:opacity (if selected? "1" ".6")}}
                                                               (str name "." (first path))]))])))
               [:<>]
               (param name-list))))

(defn- namespaces
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [data meta] :as view-props}]
       ;namespace-index {"clj" {"my-module"   {"my-namespace.clj"}}
       ;                        "your-module" {}
       ;                        "our-namespace.clj"}
  (let [namespace-index (namespace-data->namespace-index data)
        scope           (get meta :scope)]
       [:div {:style {:padding "12px 12px" :flex-shrink "0" :height "calc(100vh - 48px)" :overflow-y "auto"}}
             [:div {:style {:padding-bottom "8px" :opacity ".6"}} "Namespaces"]
             [:div {:style {:min-width "220px"}}
                   [namespace-tree surface-id view-props (get namespace-index scope) [scope]]]]))

(defn- function-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [data meta] :as view-props}]
  [:div {:style {:padding "12px 12px"}}
        [:div {:style {:padding-bottom "8px" :opacity ".6"}} "Functions"]
        (str (get-in data [(:namespace meta) :docs :functions]))])

(defn- namespace-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [data meta] :as view-props}]
  (let [namespace     (get-in data [(:namespace meta) :docs :namespace])
        created       (get-in data [(:namespace meta) :docs :created])
        version       (get-in data [(:namespace meta) :docs :version])
        author        (get-in data [(:namespace meta) :docs :author])
        compatibility (get-in data [(:namespace meta) :docs :compatibility])]

       [:div {:style {:padding "12px 12px" :opacity ".6" :line-height "22px"}}
             [:div {:style {:font-size "16px"}} namespace]
             [:div {:style {:display "flex"}}
                   (if (some? author)        [:div {:style {:font-size "13px" :min-width "250px"}} "Author: "        author])
                   (if (some? created)       [:div {:style {:font-size "13px"}} "Created: "       created])]
             [:div {:style {:display "flex"}}
                   (if (some? version)       [:div {:style {:font-size "13px" :min-width "250px"}} "Version: "       version])
                   (if (some? compatibility) [:div {:style {:font-size "13px"}} "Compatibility: " compatibility])]]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [meta] :as view-props}]
  [:div {:style {:display "flex" :width "100%" :font-family "monospace"}}
        [scopes        surface-id view-props]
        [namespaces    surface-id view-props]
        (if (some? (:namespace meta))
            [:div [namespace-header surface-id view-props]
                  [function-list    surface-id view-props]]
            [:div {:style {:font-size "16px" :padding "12px 12px" :opacity ".6"}} "No namespace selected"])])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/download-docs!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:sync/send-request! :developer/download-docs!
                       {:method      :get
                        :target-path [:docs :data]
                        :uri         "/docs/download-docs"}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/render-docs!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view :subscriber [:db/get-item [:docs]]}}])

(a/reg-event-fx
  :developer/load-docs!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (assoc-in db [:docs :meta :scope] "clj")
       :dispatch-n [[:developer/download-docs!]
                    [:developer/render-docs!]]}))
