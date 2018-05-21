/*----------Point(s) Structure-------------*/

//struct tPointStructure;
typedef struct tPointStructure tsPoint;
typedef tsPoint *tPoint;

#define EXIT_FAILURE   1
#define X 0
#define Y 1
//typedef enum { FALSE, TRUE }   bool;

#define DIM 2               /* Dimension of points */
typedef int tPointi[DIM];   /* Type integer point */

struct tPointStructure {
   int     vnum;
   tPointi v;
   bool    primary;
};


/*----------Function Prototypes-------------*/
int     AreaSign( tPointi a, tPointi b, tPointi c );
int     ReadPoints( tPointi p0 );
void    PrintPoints( void );
void    SubVec( tPointi a, tPointi b, tPointi c );
void    AddVec( tPointi a, tPointi b, tPointi c );
void    Vectorize( void );
int     Compare( const void *tp1, const void *tp2 );
void    OutputPolygons();
void    Convolve( int j0, tPointi p0 );
/*------------------------------------------*/
