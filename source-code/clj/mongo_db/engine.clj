
(ns mongo-db.engine
    (:import org.bson.types.BSONTimestamp)
    (:require [mid-fruits.candy        :refer [param return]]
              [mid-fruits.gestures     :as gestures]
              [mid-fruits.json         :as json]
              [mid-fruits.keyword      :as keyword]
              [mid-fruits.random       :as random]
              [mid-fruits.time         :as time]
              [mid-fruits.vector       :as vector]
              [mongo-db.connection     :refer [DB]]
              [monger.collection       :as mcl]
              [monger.conversion       :as mcv]
              [monger.core             :as mcr]
              [monger.operators        :as mop :refer [$regex]]
              [monger.query            :as mqr]
              [x.server-core.api       :as a]
              [x.server-db.api         :as db]
              [x.server-dictionary.api :as dictionary]
              monger.joda-time))



;; -- TODO --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @todo
;  Az add-document!, upsert-document!, update-document! és merge-document! függvények
;  a monger.collection save-and-return függvényét használják az egyes dokumentumok írására.
;  A save-and-return függvény nem minden esetben a legalkalmasabb a teljesítmény-optimalizálás
;  szempontjából!



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-LOCALE "hu")



;; -- Helpers -----------------------------------------------------------------
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

(defn- aggregation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (vector) pipeline
  ; @param (map)(opt) options
  ;  {:locale (string)(opt)
  ;    Default: DEFAULT-LOCALE}
  ;
  ; @return (maps in vector)
  ([collection-name pipeline]
   (aggregation collection-name pipeline {}))

  ([collection-name pipeline {:keys [locale]}]
   (let [locale      (or locale DEFAULT-LOCALE)
         aggregation (mcr/command @DB {:aggregate collection-name
                                       :pipeline  pipeline
                                       :collation {:locale locale :numericOrdering true}
                                       :cursor    {}})]
        (get-from-aggregation aggregation))))



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

(defn- get-all-document-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (integer)
  [collection-name]
  (mcl/count @DB collection-name))

(defn- get-document-count-by-query
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

(defn get-all-documents
  ; @param (string) collection-name
  ; @param (map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-all-documents "my-collection" {:my-namespace/my-keyword  0
  ;                                               :my-namespace/your-string 1})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (maps in vector)
  ;  [{:namespace/id (string)}]
  [collection-name & [projection]]
  (let [documents (find-all-documents collection-name projection)]
       (reduce (fn [result document]
                   (let [document (-> document (_id->id)
                                               (json/keywordize-values)
                                               (time/unparse-date-time))]
                        (vector/conj-item result document)))
               (param [])
               (param documents))))

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
        projection (json/unkeywordize-keys  projection)
        documents  (find-documents-by-query collection-name query projection)]
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
  ; Az add-document! függvény a számára azonosító nélkül átadott dokumentumot
  ; újonnan generált azonosítóval látja el az eltárolás előtt, míg a számára
  ; azonosítóval átadott dokumentumot annak azonosítójának változtatása nélkül
  ; tárolja el.
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:modifier-f (function)(opt)
  ;   :ordered? (boolean)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
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

  ([collection-name document {:keys [ordered? modifier-f prototype-f]}]
         ; A dokumentum sorrendbeli pozíciója egyenlő a hozzáadás előtti dokumentumok számával.
   (let [document-dex (get-all-document-count collection-name)

         ; A prototype-f függvény azelőtt módosítja a dokumentumot, mielőtt az add-document! függvény
         ; végrehajtana rajta bármilyen változtatást.
         document (if (some?       prototype-f)
                      (prototype-f document)
                      (return      document))

                                   ; Ha a dokumentum rendezett dokumentumként kerül hozzáadásra,
                                   ; akkor szükséges a sorrendbeli pozícióját hozzáadni
         document (cond-> document (boolean ordered?)
                                   (db/document->ordered-document document-dex)
                                   ; Ha a dokumentum nem tartalmaz azonosítót, akkor hozzáfűz
                                   ; egy generált azonosítót
                                   :assoc-id
                                   (document<-id))

         ; Ha a dokumentumot annak mentése előtt szeretnéd módosítani, használj modifier-f függvényt!
         document (if (some?      modifier-f)
                      (modifier-f document)
                      (return     document))]

        (save-document! collection-name document))))

; @usage
;  [:mongo-db/add-document! "my-collection" {...}]
(a/reg-handled-fx :mongo-db/add-document! add-document!)



;; -- Adding documents --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-documents!
  ; @param (string) collection-name
  ; @param (namespaced maps in vector) documents
  ;  [{:namespace/id (string)(opt)}]
  ; @param (map)(opt) options
  ;  {:modifier-f (function)(opt)
  ;   :ordered? (boolean)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/add-documents! "my-collection" [{...} {...}])
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  ([collection-name documents]
   (add-documents! collection-name documents {}))

  ([collection-name documents options]
   (reduce (fn [result document]
               (let [return (add-document! collection-name document options)]
                    (vector/conj-item result return))
               (param [])
               (param documents)))))

; @usage
;  [:mongo-db/add-documents! "my-collection" [{...} {...}]]
(a/reg-handled-fx :mongo-db/add-documents! add-documents!)



;; -- Upserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Ha a dokumentum nem létezik a kollekcióban, akkor az upsert-document! függvény
  ;    hozzáadja azt a kollekcióhoz az add-document! függvény használatával.
  ;    Az {:ordered? ...} tulajdonság az add-document! függvény paramétere!
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/upsert-document! "my-collection" {:my-namespace/my-keyword  :my-value
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
   (upsert-document! collection-name document {}))

  ([collection-name document {:keys [prototype-f] :as options}]
   (let [document-id (db/document->document-id document)]
        (if (document-exists? collection-name document-id)

            ; If document exists ...
            (let [document (if (some?       prototype-f)
                               (prototype-f document)
                               (return      document))]
                 (save-document! collection-name document))

            ; If document NOT exists ...
            (add-document! collection-name document options)))))

; @usage
;  [:mongo-db/upsert-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/upsert-document! upsert-document!)



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
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

  ([collection-name document {:keys [prototype-f]}]
   (let [document-id (db/document->document-id document)]
        (if (document-exists? collection-name document-id)

            ; If document exists ...
            (let [document (if (some?       prototype-f)
                               (prototype-f document)
                               (return      document))]
                 (save-document! collection-name document))

            ; If document NOT exists ...
            (println "Document does not exists error" collection-name document-id)))))

; @usage
;  [:mongo-db/update-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/update-document! update-document!)



;; -- Merging document --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn merge-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/merge-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                             :my-namespace/your-string "your-value"
  ;                                             :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (merge-document! collection-name document {}))

  ([collection-name document {:keys [prototype-f]}]
   (let [document-id (db/document->document-id document)]
        (if-let [stored-document (get-document-by-id collection-name document-id)]

                ; If document exists ...
                (let [document (merge stored-document document)
                      document (if (some?       prototype-f)
                                   (prototype-f document)
                                   (return      document))]
                     (save-document! collection-name document))

                ; If document NOT exists ...
                (println "Document does not exists error" collection-name document-id)))))

; @usage
;  [:mongo-db/merge-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/merge-document! merge-document!)



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
  ; @example
  ;  (mongo-db/remove-document "my-collection" "my-document")
  ;  =>
  ;  "my-document"
  ;
  ; @return (string)
  ([collection-name document-id]
   (remove-document! collection-name document-id {}))

  ([collection-name document-id {:keys [ordered?]}]
   (if ordered? (remove-ordered-document!   collection-name document-id)
                (remove-unordered-document! collection-name document-id))))

; @usage
;  [:mongo-db/remove-document! "my-collection" "my-document"]
(a/reg-handled-fx :mongo-db/remove-document! remove-document!)



;; -- Removing documents ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-documents!
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @example
  ;  (mongo-db/remove-documents! "my-collection" ["my-document" "your-document"])
  ;  =>
  ;  ["my-document" "your-document"]
  ;
  ; @return (string)
  ([collection-name document-ids]
   (remove-documents! collection-name document-ids {}))

  ([collection-name document-ids options]
   (reduce (fn [result document-id]
               (let [return (remove-document! collection-name document-id options)]
                    (vector/conj-item result return)))
           (param [])
           (param document-ids))))

; @usage
;  [:mongo-db/remove-documents! "my-collection" ["my-document" "your-document"]]
(a/reg-handled-fx :mongo-db/remove-document! remove-document!)



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
  ;   :language-id (keyword)
  ;   :modifier-f (function)(opt)
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [label-key modifier-f prototype-f] :as options}]
  (let [document  (get-document-by-id     collection-name document-id)
        namespace (db/document->namespace document)
        id-key    (keyword/add-namespace  namespace :id)

        ; A prototype-f függvény azelőtt módosítja a dokumentumot, mielőtt a duplicate-ordered-document!
        ; függvény végrehajtana rajta bármilyen változtatást.
        document-copy (if (some?       prototype-f)
                          (prototype-f document)
                          (return      document))

        ; A label-suffix toldalék értékével kiegészített címke asszociálása
        document-copy (if (some? label-key)
                          (let [copy-label (get-document-copy-label collection-name document-id options)]
                               (assoc document-copy label-key copy-label))
                          (return document-copy))

        ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
        document-copy (dissoc document-copy id-key)

        ; Ha a dokumentumot annak mentése előtt szeretnéd módosítani, használj modifier-f függvényt!
        document-copy (if (some?      modifier-f)
                          (modifier-f document-copy)
                          (return     document-copy))]

       (add-document! collection-name document-copy)))

(defn- duplicate-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;   :modifier-f (function)(opt)
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [label-key modifier-f prototype-f] :as options}]
  (let [document     (get-document-by-id     collection-name document-id)
        document-dex (db/get-document-value  document :order)
        namespace    (db/document->namespace document)
        id-key       (keyword/add-namespace  namespace :id)

        ; Ha a dokumentum nem rendelkezik {:namespace/order ...} tulajdonsággal ...
        document-copy-dex (try (inc document-dex)
                               (catch Exception e (println "Document corrupted error" collection-name document-id)))

        ; A prototype-f függvény azelőtt módosítja a dokumentumot, mielőtt a duplicate-ordered-document!
        ; függvény végrehajtana rajta bármilyen változtatást.
        document-copy (if (some?       prototype-f)
                          (prototype-f document)
                          (return      document))

        ; A label-suffix toldalék értékével kiegészített címke asszociálása
        document-copy (if (some? label-key)
                          (let [copy-label (get-document-copy-label collection-name document-id options)]
                               (assoc document-copy label-key copy-label))
                          (return document-copy))

                                   ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
        document-copy (-> document (dissoc id-key)
                                   ; A sorrendbeli pozíció értékének hozzáadása a másolathoz
                                   (db/document->ordered-document document-copy-dex))

        ; Ha a dokumentumot annak mentése előtt szeretnéd módosítani, használj modifier-f függvényt!
        document-copy (if (some?      modifier-f)
                          (modifier-f document-copy)
                          (return     document-copy))]


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
  ;  {:label-key (namespaced keyword)(opt)
  ;    A dokumentum melyik kulcsának értékéhez fűzze hozzá a "copy" kifejezést
  ;    Only w/ {:language-id ...}
  ;   :language-id (keyword)(opt)
  ;    Milyen nyelven használja a "copy" kifejezést a névhez hozzáfűzéskor
  ;    Only w/ {:label-key ...}
  ;   :modifier-f (function)(opt)
  ;   :ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "my-document")
  ;  =>
  ;  {:my-namespace/id "..." :my-namespace/label "My document"}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "my-document" {:label-key   :my-namespace/label
  ;                                                               :language-id :en})
  ;  =>
  ;  {:my-namespace/id "..." :my-namespace/label "My document copy"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  ([collection-name document-id]
   (duplicate-document! collection-name document-id {:ordered? false}))

  ([collection-name document-id {:keys [ordered?] :as options}]
   (if ordered? (duplicate-ordered-document!   collection-name document-id options)
                (duplicate-unordered-document! collection-name document-id options))))



;; -- Reordering collection ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-documents!
  ; @param (string) collection-name
  ; @param (vectors in vector) document-order
  ;  [[(string) document-id
  ;    (integer) document-dex]]
  ;
  ; @usage
  ;  (mongo-db/reorder-documents "my-collection" [["my-document" 1] ["your-document" 2]])
  ;
  ; @return (vectors in vector)
  [collection-name document-order]
  (let [namespace (get-collection-namespace collection-name)
        order-key (keyword/add-namespace    namespace :order)]
       (doseq [[document-id document-dex] document-order]
              (mcl/update @DB collection-name {:_id document-id}
                                              {"$set" {(keyword/to-string order-key) document-dex}}))
       (return document-order)))



;; -- Advanced DB functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (map) search-props
  ;  {:max-count (integer)
  ;   :search-pattern (vectors in vector)
  ;   [[(namespaced keyword) search-key
  ;      (string) search-term]]
  ;   :skip (integer)
  ;   :sort-pattern (vectors in vector)
  ;    [[(namespaced keyword) sort-key
  ;      (integer) sort-direction]]}
  ;
  ; @usage
  ;  (mongo-db/get-documents-by-pipeline "my-collection" {:max-count      50
  ;                                                       :search-pattern [[:fruit/label "Apple"] [...]]
  ;                                                       :skip           150
  ;                                                       :sort-pattern   [[:fruit/weight -1] [...]]})
  ;
  ; @return (maps in vector)
  [collection-name pipeline]
  (-> (aggregation collection-name pipeline nil)
      (json/keywordize-values)
      (time/unparse-date-time)))

(defn count-documents-by-pipeline
      ; @param (string) collection-name
      ; @param (map) search-props
      ;  {:search-key (namespaced keyword)
      ;   :search-term (string)}
      ;
      ; @usage
      ;  (mongo-db/count-documents-by-pipeline "my-collection" {:search-key  :my-namespace/label
      ;                                                         :search-term "My document"})
      ;
      ; @return (integer)
      [collection-name pipeline]
      (count (get-documents-by-pipeline collection-name pipeline)))
