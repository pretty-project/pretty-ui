
(ns mongo-db.engine
    (:require [mid-fruits.candy            :refer [param return]]
              [mid-fruits.gestures         :as gestures]
              [mid-fruits.json             :as json]
              [mid-fruits.keyword          :as keyword]
              [mid-fruits.random           :as random]
              [mid-fruits.string           :as string]
              [mid-fruits.vector           :as vector]
              [mongo-db.date-time          :as date-time]
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

(defn- document<-_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;
  ; @return (map)
  ;  {:_id (string)}
  [document]
  (let [document-id (random/generate-string)]
       (assoc document :_id document-id)))

(defn- id->_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;  {:id (string)}
  ;
  ; @example
  ;  (id->_id {:my-namespace/id :my-id})
  ;  => {:_id :my-id}
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
  ;  => {:my-namespace/id :my-id}
  ;
  ; @return (map)
  ;  {:id (string)}
  [document]
  (let [namespace   (db/document->namespace document)
        id-key      (keyword/add-namespace  namespace :id)
        document-id (get                    document  :_id)]
       (-> document (assoc  id-key document-id)
                    (dissoc :_id))))

(defn- ids->_ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) documents
  ;
  ; @example
  ;  (ids->_ids [{:id "my-document"} {:id "your-document"}])
  ;  => [{:_id "my-document"} {:_id "your-document"}]
  ;
  ; @return (maps in vector)
  [documents]
  (mapv id->_id documents))

(defn- _ids->ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) documents
  ;
  ; @example
  ;  (_ids->ids [{:_id "my-document"} {:_id "your-document"}])
  ;  => [{:id "my-document"} {:id "your-document"}]
  ;
  ; @return (maps in vector)
  [documents]
  (mapv _id->id documents))



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
        (date-time/unparse-date-time)
        (_ids->ids)))

(defn- search-aggregation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
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



;; -- Reading documents functions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-documents-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;  TODO ...
  ; @param (map)(opt) projection
  ;  TODO ...
  ;
  ; @return (?)
  [collection-name query & [projection]]
  (if (some? projection)
      (mcl/find-maps @DB collection-name query projection)
      (mcl/find-maps @DB collection-name query)))

(defn- get-all-documents
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map)(opt) projection
  ;  TODO ...
  ;
  ; @return (?)
  [collection-name & [projection]]
  (get-documents-by-query collection-name {} projection))

(defn- get-document-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;  TODO ...
  ;
  ; @return (map)
  [collection-name query]
  (mcl/find-one-as-map @DB collection-name query))

(defn- get-document-by-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (map)
  [collection-name document-id]
  (mcl/find-map-by-id @DB collection-name document-id))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-collection-namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (keyword)
  [collection-name]
  (let [all-documents  (get-all-documents collection-name)
        first-document (first all-documents)]
       (db/document->namespace first-document)))

(defn- get-documents-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;  TODO ...
  ;
  ; @return (integer)
  [collection-name & [query]]
  (if (some? query)
      (mcl/count @DB collection-name query)
      (mcl/count @DB collection-name)))



;; -- Finding documents functions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn find-documents
  ; @param (string) collection-name
  ; @param (map) query
  ;  TODO ...
  ; @param (map)(opt) projection
  ;  TODO ...
  ;
  ; @return (maps in vector)
  [collection-name query & [projection]]
  (let [documents (get-documents-by-query collection-name query projection)]
       (reduce (fn [result document]
                   (-> document (_id->id)
                               ; Megcsinálja a MongoDB?
                               ;(json/keywordize-keys)
                                (json/keywordize-values)
                                (date-time/unparse-date-time)))
               (param [])
               (param documents))))

(defn find-document-by-query
  ; @param (string) collection-name
  ; @param (map) query
  ;  TODO ...
  ;
  ; @return (map)
  [collection-name query]
  (let [document (get-document-by-query collection-name query)]
       (-> document (_id->id)
                   ; Megcsinálja a MongoDB?
                   ;(json/keywordize-keys)
                    (json/keywordize-values)
                    (date-time/unparse-date-time))))

(defn find-document-by-id
  ; @param (string) collection-name
  ; @param (string) document-id
  ;  TODO ...
  ;
  ; @return (map)
  [collection-name document-id]
  (let [document (get-document-by-id collection-name document-id)]
       (-> document (_id->id)
                   ; Megcsinálja a MongoDB?
                   ;(json/keywordize-keys)
                    (json/keywordize-values)
                    (date-time/unparse-date-time))))



;; -- Saving document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) document
  ;  {:_id (string)
  ;    Ha a dokumentum nem rendelkezik string típusú :_id kulcssal, akkor a MongoDB
  ;    BSON objektum típusú :_id kulcsot társít hozzá}
  ;
  ; @return (map)
  ;  {:id (string)}
  [collection-name document]
  (let [document (-> document (json/unkeywordize-keys)
                              (json/unkeywordize-values)
                              ; A dokumentumban string típusként tárolt dátumok és idők
                              ; átalakítása objektum típusra
                              (date-time/parse-date-time))
        return (mcl/save-and-return @DB collection-name document)]
       (-> return (json/keywordize-keys)
                  (json/keywordize-values)
                  (date-time/unparse-date-time)
                  (_id->id))))



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-document!
  ; @param (string) collection-name
  ; @param (map) namespaced document
  ;  {:id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @usage
  ;  (mongo-db/update-document! "my-collection" {:my-namespace/id "my-document"})
  ;
  ; @return (map)
  ;  {:id (string)}
  ([collection-name document]
   (update-document! collection-name document {}))

  ([collection-name document {:keys [ordered?]}]
   (let [; Ha a dokumentum nem rendelkezik azonosítóval, akkor új dokumentumként kerül
         ; hozzáadásra a kollekcióhoz
         document-exists? (db/document->identified-document? document)
         ; Ha a dokumentum új dokumentumként kerül hozzáadásra a kollekcióhoz, akkor
         ; a sorrendbeli pozíciója egyenlő a hozzáadás előtti dokumentumok számával.
         document-dex (get-documents-count collection-name)
         ; *
         document (cond-> document ; Ha a dokumentum rendelkezik azonosítóval, akkor
                                   ; szükséges az átalakítani MongoDB dokumentum-azonosítóra
                                   ; {:id "my-document"} => {:_id "my-document"}
                                   (boolean document-exists?)
                                   (id->_id)
                                   ; Ha a dokumentum nem rendelkezik azonosítóval, akkor
                                   ; szükséges hozzáadni MongoDB dokumentum-azonsítót
                                   ; {:_id "my-document"}
                                   (not     document-exists?)
                                   (document<-_id)
                                   ; Ha a dokumentum új és rendezett dokumentumként kerül
                                   ; hozzáadásra, akkor szükséges a sorrendbeli pozícióját
                                   ; hozzáadni
                                   (and (boolean ordered?)
                                        (not     document-exists?))
                                   (db/document->ordered-document document-dex))]

        (save-document! collection-name document))))

(a/reg-handled-fx
  :mongo-db/update-document!
  ; @param (string) collection-name
  ; @param (map) namespaced document
  ;  {:id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  (fn [[collection-name document options]]
      (update-document! collection-name document options)))



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
  ;  {:label-key (keyword)
  ;   :language-id (keyword)}
  ;
  ; @return (string)
  [collection-name document-id {:keys [label-key language-id]}]
  ; A dokumentumról készült másolat :label-key tulajdonságként átadott kulcsának
  ; értéke a label-suffix toldalék értékével kerül kiegészítése
  (let [document            (get-document-by-id collection-name document-id)
        document-label      (get document label-key)
        label-suffix        (dictionary/looked-up :copy {:language-id language-id})
        all-documents       (get-all-documents collection-name {label-key 1 :_id 0})
        all-document-labels (mapv label-key all-documents)]
       (gestures/item-label->duplicated-item-label document-label all-document-labels label-suffix)))

(defn- duplicate-unordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (keyword)
  ;   :language-id (keyword)}
  ;
  ; @return (map)
  ;  {:id (string)}
  [collection-name document-id {:keys [label-key] :as options}]
  (let [document      (get-document-by-id collection-name document-id)
        copy-label    (get-document-copy-label collection-name document-id options)
        document-copy (-> document ; label-suffix toldalék értékével kiegészített címke asszociálása
                                   (assoc label-key copy-label)
                                   ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
                                   (dissoc :_id)
                                   ; Új MongoDB dokumentum-azonosító hozzáadása a másolathoz
                                   (document<-_id))]

       (save-document! collection-name document-copy)))

(defn- duplicate-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (keyword)}
  ;
  ; @return (map)
  ;  {:id (string)}
  [collection-name document-id {:keys [label-key] :as options}]
  (let [document     (get-document-by-id collection-name document-id)
        document-dex (db/get-document-value document :order)

        ; Ha a dokumentum nem rendlekezik {:namespace/order ...} tulajdonsággal ...
        document-copy-dex (try (inc document-dex)
                               (catch Exception e (println "Document corrupted error #981" collection-name document-id)))

        copy-label    (get-document-copy-label collection-name document-id options)
        document-copy (-> document ; label-suffix toldalék értékével kiegészített címke asszociálása
                                   (assoc label-key copy-label)
                                   ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
                                   (dissoc :_id)
                                   ; Új MongoDB dokumentum-azonosító hozzáadása a másolathoz
                                   (document<-_id)
                                   ; A sorrendbeli pozíció értékének hozzáadása a másolathoz
                                   (db/document->ordered-document document-copy-dex))]

       (let [namespace (db/document->namespace document)
             order-key (keyword/add-namespace  namespace :order)]
            ; A sorrendben a dokumentum után következő más dokumentumok sorrendbeli
            ; pozíciójának értékét eggyel növeli
            (mcl/update @DB collection-name {order-key {"$gt" document-copy-dex}}
                                            {"$inc" {order-key 1}}
                                            {:multi true}))

       (save-document! collection-name document-copy)))

(defn duplicate-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (keyword)
  ;    A dokumentum melyik kulcsának értékéhez fűzze hozzá a "copy" kifejezést
  ;   :language-id (keyword)
  ;    Milyen nyelven használja a "copy" kifejezést a névhez hozzáfűzéskor
  ;   :ordered? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (mongo-db/duplicate-document! "my-collection" "my-document" {:label-key   :name
  ;                                                               :language-id :hu})
  ;
  ; @return (map)
  ;  {:id (string)}
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
  ;  (mongo/reorder-documents "my-collection" [["my-document" 1] ["your-document" 2]])
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

(defn count-documents-with-pipeline
  ; @param (string) collection-name
  ; @param (map) options
  ;  {:search-key (keyword)
  ;   :search-term (string)}
  ;
  ; @return (integer)
  [collection-name {:keys [search-key search-term]}]
  (let [search-key (keyword/to-string search-key)
        query      (if (string/nonempty? search-term)
                      ;{"$and" [{...} {...}]}
                       {search-key {"$regex" search-term "$options" "i"}})]
       (get-documents-count collection-name query)))

(defn find-documents-with-pipeline
  ; @param (string) collection-name
  ; @param (map) options
  ;  {:max-count (integer)
  ;   :search-key (keyword)
  ;   :search-term (string)
  ;   :skip (integer)
  ;   :sort-by (map)}
  ;
  ; @usage
  ;  (mongo/find-documents-with-pipeline "my-collection" {:max-count 50
  ;                                                       :search-key :my-namespace/label
  ;                                                       :search-term "Apple"
  ;                                                       :skip 150
  ;                                                       :sort-by {:my-namespace/weight -1}})
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
           (date-time/unparse-date-time))))
