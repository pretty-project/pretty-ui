
(ns mongo-db.engine
    (:require [mid-fruits.candy            :refer [param return]]
              [mid-fruits.gestures         :as gestures]
              [mid-fruits.json             :as json]
              [mid-fruits.keyword          :as keyword]
              [mid-fruits.random           :as random]
              [mid-fruits.string           :as string]
              [mid-fruits.time             :as time]
              [mid-fruits.vector           :as vector]
              [mongo-db.connection-handler :refer [DB]]
              [monger.collection           :as mcl]
              [monger.conversion           :as mcv]
              [monger.core                 :as mcr]
              [monger.operators            :as mop :refer [$regex]]
              [monger.query                :as mqr]
              [x.server-core.api           :as a]
              [x.server-db.api             :as db]
              [x.server-dictionary.api     :as dictionary]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-LOCALE "hu")



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- DBObject->edn
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DBObject) n
  ;
  ; @return (map)
  [n]
  (mcv/from-db-object n true))

(defn- id->_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;  {:namespace/id (string)}
  ;
  ; @example
  ;  (id->_id {:my-namespace/id :my-id :my-namespace/my-key "my-value"})
  ;  =>
  ;  {:_id :my-id :my-namespace/my-key "my-value"}
  ;
  ; @return (map)
  ;  {:_id (string)}
  [document]
  (let [namespace   (db/document->namespace document)
        id-key      (keyword/add-namespace  namespace :id)
        document-id (get                    document  id-key)]
       (-> document (assoc  :_id document-id)
                    (dissoc id-key))))

(defn- _id->id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;  {:_id (string)}
  ;
  ; @example
  ;  (_id->id {:_id :my-id :my-namespace/key :my-value})
  ;  =>
  ;  {:my-namespace/id :my-id :my-namespace/my-key "my-value"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [document]
  (let [namespace   (db/document->namespace document)
        id-key      (keyword/add-namespace  namespace :id)
        document-id (get                    document  :_id)]
       (-> document (assoc  id-key document-id)
                    (dissoc :_id))))

(defn- _ids->ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) documents
  ;
  ; @example
  ;  (_ids->ids [{:_id "my-document" :my-namespace/my-key "my-value"}])
  ;  =>
  ;  [{:my-namespace/id "my-document" :my-namespace/my-key "my-value"}]
  ;
  ; @return (maps in vector)
  [documents]
  (mapv _id->id documents))

(defn- document<-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;  {:namespace/id (string)(opt)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [document]
  (let [namespace (db/document->namespace document)
        id-key    (keyword/add-namespace  namespace :id)]
       (if-let [document-id (get document id-key)]
               (return document)
               (let [document-id (random/generate-string)]
                    (assoc document id-key document-id)))))



;; -- Aggregation functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-from-aggregation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DBObject) n
  ;
  ; @return (maps in vector)
  [n]
  (-> n (DBObject->edn)
        (get-in [:cursor :firstBatch])
        (time/unparse-date-time)
        (_ids->ids)))

(defn- search-aggregation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (vector) pipeline
  ; @param (map)(opt) options
  ;  {:locale (string)(opt)
  ;    Default: DEFAULT-LOCALE}
  ;
  ; @return (maps in vector)
  [collection-name pipeline {:keys [locale]}]
  (let [locale      (or locale DEFAULT-LOCALE)
        aggregation (mcr/command @DB {:aggregate collection-name
                                      :pipeline  pipeline
                                      :collation {:locale locale :numericOrdering true}
                                      :cursor    {}})]
       (get-from-aggregation aggregation)))



;; -- Saving document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) document
  ;  {:namespace/id (string)}
  ;
  ; @example
  ;  (save-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                   :my-namespace/your-string "your-value"
  ;                                   :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document]
  (let [namespace   (db/document->namespace document)
        id-key      (keyword/add-namespace  namespace :id)
        document-id (get document id-key)
        document    (-> document ; Ha a dokumentum nem rendelkezne string típusú :_id kulcssal,
                                 ; akkor a MongoDB BSON objektum típusú :_id kulcsot társítana hozzá!
                                 (id->_id)
                                 (json/unkeywordize-keys)
                                 (json/unkeywordize-values)
                                 ; A dokumentumban string típusként tárolt dátumok és idők
                                 ; átalakítása objektum típusra
                                 (time/parse-date-time))
        return (mcl/save-and-return @DB collection-name document)]
       (-> return (json/keywordize-keys)
                  (json/keywordize-values)
                  (time/unparse-date-time)
                  (_id->id))))



;; -- Reading documents functions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- find-documents-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ; @param (map)(opt) projection
  ;
  ; @example
  ;  (find-documents-by-query "my-collection" {"my-namespace/my-keyword"  "*:my-value"
  ;                                            "my-namespace/your-string" "your-value"}
  ;                                           {"my-namespace/my-keyword"  1
  ;                                            "my-namespace/your-string" 1})
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @return (maps in vector)
  ;  [{:_id (string)
  ;    :namespace/key (*)}]
  [collection-name query & [projection]]
  (if (some? projection)
      (vec (mcl/find-maps @DB collection-name query projection))
      (vec (mcl/find-maps @DB collection-name query))))

(defn- find-all-documents
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map)(opt) projection
  ;
  ; @example
  ;  (find-all-documents "my-collection" {"my-namespace/my-keyword"  1
  ;                                       "my-namespace/your-string" 1})
  ;  =>
  ;  [{:my-namespace/my-keyword  "*:my-value"
  ;    :my-namespace/your-string "your-value"
  ;    :_id                      "my-document"}]
  ;
  ; @return (maps in vector)
  ;  [{:_id (string)
  ;    :namespace/key (*)}]
  [collection-name & [projection]]
  (find-documents-by-query collection-name {} projection))

(defn- find-document-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;
  ; @example
  ;  (find-document-by-query "my-collection" {"my-namespace/my-keyword" "*:my-value"})
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @example
  ;  (find-document-by-query "my-collection" {"my-namespace/my-keyword"  "*:my-value"
  ;                                           "my-namespace/your-string" "your-value"})
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @return (map)
  ;  {:_id (string)
  ;   :namespace/key (*)}
  [collection-name query]
  (mcl/find-one-as-map @DB collection-name query))

(defn- find-document-by-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ;  (find-document-by-id "my-collection" "my-document")
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @return (map)
  ;  {:_id (string)
  ;   :namespace/key (*)}
  [collection-name document-id]
  (mcl/find-map-by-id @DB collection-name document-id))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-collection-namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (get-collection-namespace "my-collection")
  ;
  ; @return (keyword)
  [collection-name]
  (let [all-documents  (find-all-documents collection-name)
        first-document (first all-documents)]
       (db/document->namespace first-document)))

(defn- get-all-documents-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (integer)
  [collection-name]
  (mcl/count @DB collection-name))

(defn- get-documents-count-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;
  ; @return (integer)
  [collection-name query]
  (mcl/count @DB collection-name query))



;; -- Finding documents functions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-documents-by-query
  ; @param (string) collection-name
  ; @param (map) query
  ; @param (map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-documents-by-query "my-collection" {:my-namespace/my-keyword :my-value}
  ;                                                   {:my-namespace/my-keyword  0
  ;                                                    :my-namespace/your-string 1})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (maps in vector)
  ;  [{:namespace/id (string)}]
  [collection-name query & [projection]]
  (let [query      (-> query (json/unkeywordize-keys)
                             (json/unkeywordize-values))
        projection (json/unkeywordize-keys   projection)
        documents  (find-documents-by-query  collection-name query projection)]
       (reduce (fn [result document]
                   (let [document (-> document (_id->id)
                                               (json/keywordize-values)
                                               (time/unparse-date-time))]
                        (vector/conj-item result document)))
               (param [])
               (param documents))))

(defn get-document-by-query
  ; @param (string) collection-name
  ; @param (map) query
  ;
  ; @usage
  ;  (mongo-db/get-document-by-query "my-collection" {:my-namespace/my-keyword :my-value})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name query]
  (let [query    (-> query (json/unkeywordize-keys)
                           (json/unkeywordize-values))
        document (find-document-by-query collection-name query)]
       (-> document (_id->id)
                    (json/keywordize-values)
                    (time/unparse-date-time))))

(defn get-document-by-id
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ;  (mongo-db/get-document-by-id "my-collection" "my-document")
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document-id]
  (if-let [document (find-document-by-id collection-name document-id)]
          (-> document (_id->id)
                       (json/keywordize-values)
                       (time/unparse-date-time))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection-name document-id]
  (let [document (find-document-by-id collection-name document-id)]
       (some? document)))



;; -- Adding document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-document!
  ; @param (string) collection-name
  ; @param (map) namespaced document
  ;  {:namespace/id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @example
  ;  (mongo-db/add-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                           :my-namespace/your-string "your-value"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "240b9771-05f2-4c06-8f64-177468e2c730"}
  ;
  ; @example
  ;  (mongo-db/add-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                           :my-namespace/your-string "your-value"
  ;                                           :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (add-document! collection-name document {}))

  ([collection-name document {:keys [ordered?]}]
   (let [; A dokumentum sorrendbeli pozíciója egyenlő a hozzáadás előtti dokumentumok számával.
         document-dex (get-all-documents-count collection-name)
         document     (cond-> document ; Ha a dokumentum rendezett dokumentumként kerül hozzáadásra,
                                       ; akkor szükséges a sorrendbeli pozícióját hozzáadni
                                       (boolean ordered?)
                                       (db/document->ordered-document document-dex)
                                       ; Ha a dokumentum nem tartalmaz azonosítót, akkor hozzáfűz
                                       ; egy generált azonosítót
                                       :assoc-id
                                       (document<-id))]
        (save-document! collection-name document))))

; @usage
;  [:mongo-db/add-document! "my-collection" {...}]
(a/reg-handled-fx :mongo-db/add-document! add-document!)



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-document!
  ; @param (string) collection-name
  ; @param (map) namespaced document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @example
  ;  (mongo-db/update-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                              :my-namespace/your-string "your-value"
  ;                                              :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (update-document! collection-name document {}))

  ([collection-name document {:keys [ordered?] :as options}]
   (let [document-id (db/document->document-id document)]
        (if (document-exists? document-id)
            (save-document! collection-name document)
            (add-document!  collection-name document options)))))

; @usage
;  [:mongo-db/update-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/update-document! update-document!)



;; -- Removing document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- remove-unordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (string)
  [collection-name document-id]
  (mcl/remove-by-id @DB collection-name document-id)
  (return document-id))

(defn- remove-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (string)
  [collection-name document-id]
  (let [document     (get-document-by-id collection-name document-id)
        document-dex (db/document->document-dex document)
        namespace    (db/document->namespace    document)
        order-key    (keyword/add-namespace     namespace :order)]
       ; A sorrendben a dokumentum után következő más dokumentumok sorrendbeli
       ; pozíciójának értékét eggyel csökkenti
       (mcl/update @DB collection-name {order-key {"$gt" document-dex}}
                                       {"$inc" {order-key -1}}
                                       {:multi true})
       (remove-unordered-document! collection-name document-id)))

(defn remove-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @return (string)
  ([collection-name document-id]
   (remove-document! collection-name document-id {}))

  ([collection-name document-id {:keys [ordered?]}]
   (if (boolean ordered?)
       (remove-ordered-document!   collection-name document-id)
       (remove-unordered-document! collection-name document-id))))



;; -- Duplicating document ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-document-copy-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;   :language-id (keyword)}
  ;
  ; @return (string)
  [collection-name document-id {:keys [label-key language-id]}]
  ; A dokumentumról készült másolat :label-key tulajdonságként átadott kulcsának
  ; értéke a label-suffix toldalék értékével kerül kiegészítése
  (let [document            (get-document-by-id   collection-name document-id)
        document-label      (get                  document label-key)
        label-suffix        (dictionary/looked-up :copy {:language-id language-id})
        all-documents       (find-all-documents   collection-name)
        all-document-labels (mapv                 label-key all-documents)]
       (gestures/item-label->duplicated-item-label document-label all-document-labels label-suffix)))

(defn- duplicate-unordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;   :language-id (keyword)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [label-key] :as options}]
  (let [document      (get-document-by-id      collection-name document-id)
        copy-label    (get-document-copy-label collection-name document-id options)
        namespace     (db/document->namespace  document)
        id-key        (keyword/add-namespace   namespace :id)
        document-copy (-> document ; label-suffix toldalék értékével kiegészített címke asszociálása
                                   (assoc label-key copy-label)
                                   ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
                                   (dissoc id-key))]
       (add-document! collection-name document-copy)))

(defn- duplicate-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [label-key] :as options}]
  (let [document     (get-document-by-id    collection-name document-id)
        document-dex (db/get-document-value document :order)
        namespace     (db/document->namespace  document)
        id-key        (keyword/add-namespace   namespace :id)

        ; Ha a dokumentum nem rendelkezik {:namespace/order ...} tulajdonsággal ...
        document-copy-dex (try (inc document-dex)
                               (catch Exception e (println "Document corrupted error #981" collection-name document-id)))

        copy-label    (get-document-copy-label collection-name document-id options)
        document-copy (-> document ; label-suffix toldalék értékével kiegészített címke asszociálása
                                   (assoc label-key copy-label)
                                   ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
                                   (dissoc id-key)
                                   ; A sorrendbeli pozíció értékének hozzáadása a másolathoz
                                   (db/document->ordered-document document-copy-dex))]

       (let [namespace (db/document->namespace document)
             order-key (keyword/add-namespace  namespace :order)]
            ; A sorrendben a dokumentum után következő más dokumentumok sorrendbeli
            ; pozíciójának értékét eggyel növeli
            (mcl/update @DB collection-name {order-key {"$gt" document-copy-dex}}
                                            {"$inc" {order-key 1}}
                                            {:multi true}))

       (add-document! collection-name document-copy)))

(defn duplicate-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;    A dokumentum melyik kulcsának értékéhez fűzze hozzá a "copy" kifejezést
  ;   :language-id (keyword)
  ;    Milyen nyelven használja a "copy" kifejezést a névhez hozzáfűzéskor
  ;   :ordered? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "my-document" {:label-key   :my-namespace/label
  ;                                                               :language-id :en})
  ;  => 
  ;  {:my-namespace/id "..." :my-namespace/label "My document copy"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [ordered?] :as options}]
  (if (boolean ordered?)
      (duplicate-ordered-document!   collection-name document-id options)
      (duplicate-unordered-document! collection-name document-id options)))



;; -- Reordering collection ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-documents!
  ; @param (string) collection-name
  ; @param (vectors in vector) updated-document-order
  ;  [[(string) document-id
  ;    (integer) document-dex]]
  ;
  ; @usage
  ;  (mongo-db/reorder-documents "my-collection" [["my-document" 1] ["your-document" 2]])
  ;
  ; @return (vectors in vector)
  [collection-name updated-document-order]
  (let [namespace (get-collection-namespace collection-name)
        order-key (keyword/add-namespace    namespace :order)]
       (doseq [[document-id document-dex] updated-document-order]
              (mcl/update @DB collection-name {:_id document-id}
                                              {"$set" {(keyword/to-string order-key) document-dex}}))
       (return updated-document-order)))



;; -- Advanced DB functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn count-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (map) options
  ;  {:search-key (namespaced keyword)
  ;   :search-term (string)}
  ;
  ; @usage
  ;  (mongo-db/count-documents-by-pipeline "my-collection" {:search-key  :my-namespace/label
  ;                                                         :search-term "My document"})
  ;
  ; @return (integer)
  [collection-name {:keys [search-key search-term]}]
  (let [search-key (keyword/to-string search-key)
        query      (if (string/nonempty? search-term)
                      ;{"$and" [{...} {...}]}
                       {search-key {"$regex" search-term "$options" "i"}})]
       (get-documents-count-by-query collection-name query)))

(defn find-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (map) options
  ;  {:max-count (integer)
  ;   :search-key (namespaced keyword)
  ;   :search-term (string)
  ;   :skip (integer)
  ;   :sort-by (map)}
  ;
  ; @usage
  ;  (mongo-db/find-documents-by-pipeline "my-collection" {:max-count   50
  ;                                                        :search-key  :my-namespace/label
  ;                                                        :search-term "Apple"
  ;                                                        :skip        150
  ;                                                        :sort-by     {:my-namespace/weight -1}})
  ;
  ; @return (maps in vector)
  [collection-name {:keys [max-count search-key search-term skip sort-by] :as options}]
  (let [search-key (keyword/to-string search-key)
        query      (if (string/nonempty? search-term)
                       {search-key {"$regex" search-term "$options" "i"}})
        pipeline   [{"$match" query}
                    {"$sort"  sort-by}
                    {"$skip"  skip}
                    {"$limit" max-count}]]
       (-> (search-aggregation collection-name pipeline)
           (json/keywordize-values)
           (time/unparse-date-time))))
