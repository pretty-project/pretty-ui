
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.core.subs
    (:require [candy.api                            :refer [return]]
              [engines.engine-handler.body.subs     :as body.subs]
              [engines.engine-handler.errors.subs   :as errors.subs]
              [engines.engine-handler.transfer.subs :as transfer.subs]
              [map.api                              :as map]
              [re-frame.api                         :refer [r]]
              [vector.api                           :as vector]
              [x.activities.api                     :as x.activities]
              [x.sync.api                           :as x.sync]))



;; -- Meta-items subscriptions ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ engine-id item-key]]
  (get-in db [:engines :engine-handler/meta-items engine-id item-key]))



;; -- Request subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) request-key
  ;
  ; @example
  ; (r get-request-id db :my-engine :browser)
  ; =>
  ; :my-handler/synchronize-browser!
  ;
  ; @return (keyword)
  [db [_ engine-id request-key]]
  ; XXX#5476
  ; Normally the engines use one certain request-id for different requests.
  ; That's because in this way it's easier to check whether the engine has
  ; any ongoing request or not.
  ; (this way the engine-synchronizing? function has to check only one request-id
  ;  per engine)
  ;
  ; If necessary it's possible to use different id-s for different requests by
  ; setting an individual request-key for each requests.
  ;
  ; E.g. The item-browser engine sends two individual requests at the same time
  ;      when downloading the browsed item and the listed items. It sends its own
  ;      'request-item!' request and sends the item-lister engine's 'request-items!'
  ;      request at the same time.
  ;      If each two request uses the same id, the item-lister engine's request
  ;      would be ignored to avoid unnecessary downloadings.
  (if-let [handler-key (r transfer.subs/get-transfer-item db engine-id :handler-key)]
          (keyword (str                (name handler-key))
                   (str "synchronize-" (name request-key) "!"))
          (r errors.subs/print-missing-handler-key db engine-id)))

(defn engine-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) request-key
  ;
  ; @usage
  ; (r engine-synchronizing? db :my-engine :browser)
  ;
  ; @return (boolean)
  [db [_ engine-id request-key]]
  ; XXX#5476
  (let [request-id (r get-request-id db engine-id request-key)]
       (r x.sync/listening-to-request? db request-id)))



;; -- Single item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ engine-id item-id]]
  ; XXX#6487
  (if-let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
          (conj items-path item-id)))

(defn get-downloaded-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  ; XXX#6487
  (if-let [item-path (r get-item-path db engine-id item-id)]
          (get-in db item-path)))

(defn export-downloaded-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ engine-id item-id]]
  ; XXX#6487
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (if-let [item (r get-downloaded-item db engine-id item-id)]
          (let [item-namespace (r transfer.subs/get-transfer-item db engine-id :item-namespace)]
               (map/add-namespace item item-namespace))))

(defn item-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  (let [item (r get-downloaded-item db engine-id item-id)]
       (boolean item)))



;; -- Current item subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-default-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ engine-id]]
  (r body.subs/get-body-prop db engine-id :default-item-id))

(defn get-current-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ engine-id]]
  (r get-meta-item db engine-id :item-id))

(defn get-current-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (vector)
  [db [_ engine-id]]
  ; XXX#6487
  (if-let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
          (let [current-item-id (r get-current-item-id db engine-id)]
               (conj items-path current-item-id))))

(defn no-item-id-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [current-item-id (r get-meta-item db engine-id :item-id)]
       (nil? current-item-id)))

(defn current-item-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [current-item-id (r get-current-item-id db handler-id)]
       (r item-downloaded? db handler-id current-item-id)))

(defn current-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  ; XXX#0079
  ; Checks whether an engine is handling a certain item or not.
  ;
  ; Returns true if ...
  ; ... the item with the given item-id is currently loaded into the engine.
  (= item-id (r get-meta-item db engine-id :item-id)))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  ; XXX#6487
  ; In subscriptions it's important to check the accessibility of the used body properties!
  ; E.g. If a Reagent component subscribes before the engine's body component mounted
  ;      into the React tree, the body component's properties cannot accessible.
  ;
  ; In a case like that, when the items-path isn't accessible and its value is NIL,
  ; the get-in function takes a NIL as the path and its return value would
  ; be the whole db!
  (if-let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
          (let [item-id (r get-current-item-id db engine-id)]
               (item-id (get-in db items-path)))))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (namespaced map)
  [db [_ engine-id]]
  ; XXX#6487
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (if-let [current-item-id (r get-current-item-id db engine-id)]
          (r export-downloaded-item db engine-id current-item-id)))

(defn get-current-item-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ engine-id item-key]]
  ; XXX#6487
  (if-let [current-item (r get-current-item db engine-id)]
          (let [item-id    (r get-current-item-id     db engine-id)
                items-path (r body.subs/get-body-prop db engine-id :items-path)]
               (get-in db (conj items-path item-id item-key)))))

(defn get-current-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (metamorphic-content)
  [db [_ engine-id]]
  ; XXX#6487
  (if-let [current-item (r get-current-item db engine-id)]
          (let [label-key (r body.subs/get-body-prop db engine-id :label-key)]
               (label-key current-item))))

(defn get-current-item-modified-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ engine-id]]
  ; XXX#6487
  (if-let [current-item (r get-current-item db engine-id)]
          (if-let [modified-at (:modified-at current-item)]
                  (r x.activities/get-actual-timestamp db modified-at))))

(defn get-auto-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  [db [_ engine-id]]
  ; XXX#6487 + the 'auto-title?' is an optional property.
  (if-let [auto-title? (r body.subs/get-body-prop db engine-id :auto-title?)]
          (r get-current-item-label db engine-id)))



;; -- Current view subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-default-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ engine-id]]
  (r body.subs/get-body-prop db engine-id :default-view-id))

(defn get-current-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (keyword)
  [db [_ engine-id]]
  (r get-meta-item db engine-id :view-id))

(defn no-view-id-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [current-view-id (r get-meta-item db engine-id :view-id)]
       (nil? current-view-id)))



;; -- Multiple items subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @usage
  ; (r get-item-order db :my-engine)
  ;
  ; @return (strings in vector)
  [db [_ engine-id]]
  (r get-meta-item db engine-id :item-order))

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  ; XXX#6487
  (if-let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
          (get-in db items-path)))

(defn get-listed-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @usage
  ; (r get-listed-items db :my-engine)
  ;
  ; @return (maps in vector)
  [db [_ engine-id]]
  (let [item-order (r get-item-order db engine-id)]
       (letfn [(f [item-id] (r get-downloaded-item db engine-id item-id))]
              (vector/->items item-order f))))

(defn get-listed-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @usage
  ; (r get-listed-item-count db :my-engine)
  ;
  ; @return (integer)
  [db [_ engine-id]]
  (let [listed-items (r get-listed-items db engine-id)]
       (count listed-items)))

(defn export-listed-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (namespaced maps in vector)
  [db [_ engine-id]]
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (let [item-order (r get-item-order db engine-id)]
       (vector/->items item-order (fn [%] (r export-downloaded-item db engine-id %)))))

(defn item-listed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  (let [item-order (r get-item-order db engine-id)]
       (vector/contains-item? item-order item-id)))



;; -- Query subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-query-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (vector) query
  ;
  ; @usage
  ; [body :my-engine {:query [:my-query]}]
  ; (r use-query-prop db :my-engine [...])
  ;
  ; @return (vector)
  ; [:my-query ...]
  [db [_ engine-id query]]
  ; XXX#7059
  ; The body components of the engines can take the {:query [...]} property,
  ; which is an additional query used for downloading additional data in the
  ; same request when the engine downloading the item(s).
  ;
  ; This function joins the additional query to the query passed by as its argument.
  (if-let [query-prop (r body.subs/get-body-prop db engine-id :query)]
          (vector/concat-items query-prop query)
          (return                         query)))

(defn use-query-params
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) query-props
  ;
  ; @usage
  ; (r use-query-params db :my-engine {...})
  ;
  ; @return (map)
  [db [_ engine-id query-props]]
  ; XXX#7061
  ; When an engine sends a query (mutation or resolver), the previously set
  ; query parameters will be merged into the actual parameters of the query.
  ;
  ; This is very useful when an engine uses another engines' queries and requests
  ; and it's necessary to put some additional parameter to the other engine's query.
  ; In this case, the engine which uses other engine's queries, has to put its
  ; parameter into the :query-params meta item and the other engine's query function
  ; will find it and merge the additional parameters into its query.
  ;
  ; Another usecase, when there are some default parameter which has to be sent
  ; with every query. The :query-params meta item is the perfect place to store
  ; the default query parameters because every query function will find it and
  ; merge it into its query.
  (let [query-params (get-in db [:engines :engine-handler/meta-items engine-id :query-params])]
       (merge query-params query-props)))
