
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.06
; Description:
; Version: v0.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.uri
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.reader :as reader]
              [mid-fruits.string :as string]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn valid-path
  ; @param (string) path
  ;
  ; @example
  ;  (uri/valid-path "my-route")
  ;  =>
  ;  "/my-route"
  ;
  ; @example
  ;  (uri/valid-path "/my-route")
  ;  =>
  ;  "/my-route"
  ;
  ; @example
  ;  (uri/valid-path "/my-route/")
  ;  =>
  ;  "/my-route"
  ;
  ; @return (string)
  [path]   ; 1.
  (-> path (string/not-ends-with! "/")
           ; 2.
           (string/starts-with!   "/")))

(defn uri->protocol
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->protocol "https://something.com/scooby-doo")
  ;  =>
  ;  "https"
  ;
  ; @example
  ;  (uri/uri->protocol "something.com/scooby-doo")
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [uri]
  (string/before-first-occurence uri "://"))

(defn uri->tail
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->tail "https://something.com/scooby-doo?a=b&c#d")
  ;  =>
  ;  "a=b&c#d"
  ;
  ; @example
  ;  (uri/uri->tail "https://something.com/scooby-doo#d")
  ;  =>
  ;  "d"
  ;
  ; @example
  ;  (uri/uri->tail "https://something.com/scooby-doo")
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [uri]
  (if (string/contains-part?        uri "?")
      (string/after-first-occurence uri "?")
      (string/after-first-occurence uri "#")))

(defn- uri->trimmed-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->trimmed-uri "https://something.com/scooby-doo?a=b&c&d")
  ;  =>
  ;  "something.com/scooby-doo"
  ;
  ; @return (string)
  [uri]
  (let [protocol (uri->protocol uri)
        tail     (uri->tail uri)]
       (-> uri (string/not-starts-with! (str protocol "://"))
               (string/not-ends-with!   (str "?" tail)))))

(defn uri->path
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->path "https://something.com/scooby-doo?a=b&c")
  ;  =>
  ;  "/scooby-doo"
  ;
  ; @example
  ;  (uri/uri->path "https://something.com/?a=b&c")
  ;  =>
  ;  "/"
  ;
  ; @example
  ;  (uri/uri->path "https://something.com?a=b&c")
  ;  =>
  ;  "/"
  ;
  ; @return (string)
  [uri]
  (let [trimmed-uri (uri->trimmed-uri uri)]
       (if (string/contains-part? trimmed-uri "/")
           (str    "/" (string/after-first-occurence trimmed-uri "/"))
           (return "/"))))

(defn- uri->trimmed-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->trimmed-path "https://something.com/scooby-doo?a=b&c")
  ;  =>
  ;  "scooby-doo"
  ;
  ; @example
  ;  (uri/uri->trimmed-path "https://something.com/scooby-doo/")
  ;  =>
  ;  "scooby-doo"
  ;
  ; @return (string)
  [uri]
  (let [path (uri->path uri)]
       (-> path (string/not-starts-with! "/")
                (string/not-ends-with!   "/"))))

(defn uri->path-parts
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->path-parts "https://something.com/scooby-doo?a=b&c")
  ;  =>
  ;  ["scooby-doo"]
  ;
  ; @example
  ;  (uri/uri->path-parts "https://something.com/scooby-doo/where-are-you")
  ;  =>
  ;  ["scooby-doo" "where-are-you"]
  ;
  ; @example
  ;  (uri/uri->path-parts "https://something.com/")
  ;  =>
  ;  []
  ;
  ; @return (vector)
  [uri]
  (let [trimmed-path (uri->trimmed-path uri)]
       (string/split trimmed-path #"/")))

(defn uri->path-params
  ; @param (string) uri
  ; @param (string) template
  ;
  ; @example
  ;  (uri/uri->path-params "https://something.com/scooby-doo/where-are-you"
  ;                        "/:a/:b")
  ;  =>
  ;  {:a "scooby-doo" :b "where-are-you"}
  ;
  ; @example
  ;  (uri/uri->path-params "https://something.com/scooby-doo/where-are-you"
  ;                        "/:a/b")
  ;  =>
  ;  {:a "scooby-doo"}
  ;
  ; @example
  ;  (uri/uri->path-params "/scooby-doo/where-are-you"
  ;                        "/:a/:b")
  ;  =>
  ;  {:a "scooby-doo" :b "where-are-you"}
  ;
  ; @example
  ;  (uri/uri->path-params "/scooby-doo/where-are-you"
  ;                        "/a/b")
  ;  =>
  ;  {}
  ;
  ; @return (map)
  [uri template]
  (let [path           (uri->path       uri)
        path-parts     (uri->path-parts path)
        template-parts (uri->path-parts template)]
       (letfn [(uri->path-params-f [o dex x]
                                   (let [x (reader/string->mixed x)]
                                        (if (keyword? x)
                                            (let [path-part (nth path-parts dex)]
                                                 (assoc o x path-part))
                                            (return o))))]
              (reduce-kv uri->path-params-f {} template-parts))))

(defn uri->fragment
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->fragment "https://something.com/scooby-doo?a=b&c#d")
  ;  =>
  ;  "d"
  ;
  ; @example
  ;  (uri/uri->fragment "https://something.com/scooby-doo?a=b&c")
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [uri]
  (string/after-first-occurence uri "#"))

(defn uri->uri-before-fragment
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->uri-before-fragment "https://something.com/scooby-doo?a=b&c#d")
  ;  =>
  ;  "https://something.com/scooby-doo?a=b&c"
  ;
  ; @example
  ;  (uri/uri->uri-before-fragment "https://something.com/scooby-doo?a=b&c")
  ;  =>
  ;  "https://something.com/scooby-doo?a=b&c"
  ;
  ; @return (string)
  [uri]
  (if (string/contains-part?         uri "#")
      (string/before-first-occurence uri "#")
      (return                        uri)))

(defn uri->query-string
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->query-string "https://something.com/scooby-doo?a=b&c#d")
  ;  =>
  ;  "a=b&c"
  ;
  ; @example
  ;  (uri/uri->query-string "https://something.com/scooby-doo#d")
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [uri]
  (let [uri-before-fragment (uri->uri-before-fragment uri)]
       (if (string/contains-part?        uri-before-fragment "?")
           (string/after-first-occurence uri-before-fragment "?"))))

(defn uri->query-params
  ; @param (string) uri
  ;
  ; @example
  ;  (uri/uri->query-params "http://something.com/scooby-doo?a=b&c#d")
  ;   =>
  ;  {:a "b" :c nil}
  ;
  ; @example
  ;  (uri/uri->query-params "http://something.com/scooby-doo#d")
  ;   =>
  ;  {}
  ;
  ; @return (map)
  [uri]
  (let [query-string (uri->query-string uri)]
       (letfn [(uri->query-params-f [o x]
                                    (let [k-v (string/split x #"=")
                                          k   (keyword (first  k-v))
                                          v            (second k-v)]
                                         (assoc o k v)))]
              (reduce uri->query-params-f {} (string/split query-string #"&")))))

(defn string->uri
  ; @param (string) n
  ;
  ; @example
  ;  (uri/string->uri "something.com/scooby doo?where are you")
  ;  =>
  ;  "something.com/scooby%20doo?where%20are%20you"
  ;
  ; @return (string)
  [n]
  #?(:cljs (.encodeURI js/window n)))
    ;:clj TODO ...

(defn string->uri-part
  ; @param (string) n
  ;
  ; @example
  ;  (uri/string->uri "something.com/scooby doo?where are you")
  ;  =>
  ;  "something.com%2Fscooby%20doo%3Fwhere%20are%20you"
  ;
  ; @return (string)
  [n]
  #?(:cljs (.encodeURIComponent js/window n)))
    ;:clj TODO ...

(defn uri<-query-param
  ; @param (string) uri
  ; @param (string) query-param
  ;
  ; @example
  ;  (uri/uri<-query-param "something.com/scooby-doo" "where")
  ;  =>
  ;  "something.com/scooby-doo?where"
  ;
  ; @example
  ;  (uri/uri<-query-param "something.com/scooby-doo" "where=are")
  ;  =>
  ;  "something.com/scooby-doo?where=are"
  ;
  ; @example
  ;  (uri/uri<-query-param "something.com/scooby-doo#you" "where")
  ;  =>
  ;  "something.com/scooby-doo?where#you"
  ;
  ; @example
  ;  (uri/uri<-query-param "something.com/scooby-doo?you" "where=are")
  ;  =>
  ;  "something.com/scooby-doo?you&where=are"
  ;
  ; @return (string)
  [uri query-param]
  (let [fragment     (uri->fragment     uri)
        query-string (uri->query-string uri)]
       (cond ; uri contains fragment & query-string ...
             (and (some? fragment)
                  (some? query-string))
             (str (string/before-first-occurence uri "?")
                  (str "?" query-string "&" query-param "#" fragment))
             ; uri contains fragment ...
             (and (some? fragment)
                  (nil?  query-string))
             (str (string/before-first-occurence uri "#")
                  (str "?" query-param "#" fragment))
             ; uri contains query-string ...
             (and (nil?  fragment)
                  (some? query-string))
             (str (string/before-first-occurence uri "?")
                  (str "?" query-string "&" query-param))
             ; uri NOT contains fragment or query-string ...
             (and (nil? fragment)
                  (nil? query-string))
             (str uri "?" query-param))))
